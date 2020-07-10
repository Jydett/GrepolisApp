package fr.jydet.grepolis.io;

import fr.jydet.grepolis.dao.DaoRegistry;
import fr.jydet.grepolis.dao.alliance.AllianceDao;
import fr.jydet.grepolis.dao.island.IslandDao;
import fr.jydet.grepolis.dao.player.PlayerDao;
import fr.jydet.grepolis.io.dto.Dto;
import fr.jydet.grepolis.model.*;
import fr.jydet.grepolis.utils.ConsoleLoadingBar;
import lombok.AllArgsConstructor;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CSVToDatabase {

    private final AllianceDao allianceDao;
    private final PlayerDao playerDao;
    private final IslandDao islandDao;

    public CSVToDatabase() {
        playerDao = DaoRegistry.getDao(Player.class);
        allianceDao = DaoRegistry.getDao(Alliance.class);
        islandDao = DaoRegistry.getDao(Island.class);
    }

    public CSVToDatabase saveAlliances(List<Dto.AllianceDto> allianceDtos,
                                       List<Dto.KillRankDto> allianceKillAllDtos,
                                       List<Dto.KillRankDto> allianceKillDefDtos,
                                       List<Dto.KillRankDto> allianceKillAttDtos
                             ) {
        List<ObjectWithStats<Dto.AllianceDto>> alliancesFullDtos = join(
                allianceDtos, allianceKillAllDtos, allianceKillDefDtos, allianceKillAttDtos);

        for (ObjectWithStats<Dto.AllianceDto> dto : alliancesFullDtos) {
            Dto.AllianceDto allianceDto = dto.object;
            Alliance alliance = new Alliance(allianceDto.getId(), allianceDto.getName(), allianceDto.getPoints(),
                allianceDto.getVillagesCount(), allianceDto.getMembersCount(),
                allianceDto.getRank(),
                new KillsRank(dto.all.getRank(), dto.all.getPoint()),
                new KillsRank(dto.def.getRank(), dto.def.getPoint()),
                new KillsRank(dto.att.getRank(), dto.att.getPoint()));
            allianceDao.save(alliance);
        }
        return this;
    }

    public CSVToDatabase savePlayers(List<Dto.PlayerDto> playerDtos,
                             List<Dto.KillRankDto> playerKillAllDtos,
                             List<Dto.KillRankDto> playerKillDefDtos,
                             List<Dto.KillRankDto> playerKillAttDtos) {

        List<ObjectWithStats<Dto.PlayerDto>> alliancesFullDtos = join(
                playerDtos, playerKillAllDtos, playerKillDefDtos, playerKillAttDtos);

        List<Player> players = new ArrayList<>(50);
        for (ObjectWithStats<Dto.PlayerDto> dto : alliancesFullDtos) {
            Player player = dto.object.toPlayer(allianceDao);
            player.setAllKillsRank(new KillsRank(dto.all.getRank(), dto.all.getPoint()));
            player.setAttackKillsRank(new KillsRank(dto.att.getRank(), dto.att.getPoint()));
            player.setDefKillsRank(new KillsRank(dto.def.getRank(), dto.def.getPoint()));
            players.add(player);
        }
        playerDao.saveAll(players);
        return this;
    }

    public void saveIslands(List<Dto.IslandDto> islandDtos) {
        int size = islandDtos.size();
        Instant begin = Instant.now();
        ConsoleLoadingBar.printPercent("SaveIsland :", 0, size);
        for (int i = 0; i < size; i++) {
            ConsoleLoadingBar.printPercentWithETA(null, i, size, begin);
            Dto.IslandDto dto = islandDtos.get(i);
            Island island = new Island(dto.getId(), dto.getX(), dto.getY(), dto.getIslandType(),
                    dto.getAvailableTown(), dto.getPlusRessources(), dto.getMinusRessources());
            islandDao.save(island);
        }
        ConsoleLoadingBar.printPercentWithETA(null, size, size, begin);
    }

    public void saveIslandsFast(String path) {
        File file = new File(path);
        islandDao.saveFast(file);
    }

    public void saveFast(CSVDownloader.DataType dataType, String path) {
        switch (dataType) {
            case ISLANDS:
                saveIslandsFast(path);
                return;
            case PLAYERS:
            case ALLIANCES:
            case TOWNS:
            case PLAYER_KILLS_ALL:
            case PLAYER_KILLS_ATT:
            case PLAYER_KILLS_DEF:
            case ALLIANCE_KILLS_ALL:
            case ALLIANCE_KILLS_ATT:
            case ALLIANCE_KILLS_DEF:
            case CONQUERS:
                throw new UnsupportedOperationException();
        }
    }

    @AllArgsConstructor
    public static class ObjectWithStats<A> {
        public A object;
        public Dto.KillRankDto all;
        public Dto.KillRankDto def;
        public Dto.KillRankDto att;
    }

    private static <D extends Dto> List<ObjectWithStats<D>> join(List<D> dtos,
                                                          List<Dto.KillRankDto> killAllDtos,
                                                          List<Dto.KillRankDto> killDefDtos,
                                                          List<Dto.KillRankDto> killAttDtos) {
        return dtos.stream().map(
            allianceDto -> {
                String id = allianceDto.getIdStr();
                return new ObjectWithStats<>(
                        allianceDto,
                        killAllDtos.stream().filter(k -> k.getIdStr().equals(id)).findAny().get(),
                        killDefDtos.stream().filter(k -> k.getIdStr().equals(id)).findAny().get(),
                        killAttDtos.stream().filter(k -> k.getIdStr().equals(id)).findAny().get()
                );
            }).collect(Collectors.toList());
    }

}
