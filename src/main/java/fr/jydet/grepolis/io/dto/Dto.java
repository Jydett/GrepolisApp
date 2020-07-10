package fr.jydet.grepolis.io.dto;

import fr.jydet.grepolis.dao.alliance.AllianceDao;
import fr.jydet.grepolis.model.KillsRank;
import fr.jydet.grepolis.model.Player;
import fr.jydet.grepolis.model.Ressources;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static fr.jydet.grepolis.utils.ParseUtils.*;

public interface Dto {
    Function<String[], PlayerDto> playerTransformer = lines -> new PlayerDto(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5]);

    String getIdStr();

    @AllArgsConstructor
    class PlayerDto implements Dto {
        private String id;
        @Getter
        private String name;
        private String alliance_id;
        private String points;
        private String rank;
        private String town;

        public Long getId() {
            return Long.parseLong(id);
        }

        public String getIdStr() {
            return id;
        }

        public Integer getTownCount() {
            return Integer.parseInt(town);
        }

        public Long getAllianceId() {
            return asLongOrEmpty(alliance_id);
        }

        public Long getPoints() {
            return Long.parseLong(points);
        }

        public Integer getRank() {
            return Integer.parseInt(rank);
        }

        public Player toPlayer(AllianceDao allianceDao) {
            return new Player(
                getId(),
                name,
                alliance_id.isEmpty() ? null : allianceDao.proxy(getAllianceId()),
                getPoints(),
                getRank(),
                null,
                null,
                null,
                getTownCount()
            );
        }
    }

    Function<String[], AllianceDto> allianceTransformer = lines -> new AllianceDto(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5]);

    @Getter
    @AllArgsConstructor
    class AllianceDto implements Dto {
        private String id;
        private String name;
        private String points;
        private String villagesCount;
        private String membersCount;
        private String rank;

        public Long getId() {
            return Long.parseLong(id);
        }

        public String getIdStr() {
            return id;
        }

        public Long getPoints() {
            return Long.parseLong(points);
        }

        public Integer getVillagesCount() {
            return Integer.valueOf(villagesCount);
        }

        public Integer getMembersCount() {
            return Integer.valueOf(membersCount);
        }

        public Long getRank() {
            return Long.valueOf(rank);
        }
    }

    Function<String[], IslandDto> islandsTransformer = lines -> new IslandDto(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6]);


    //LOAD DATA LOCAL INFILE "K:/save arthur/Arthur/GrepolisApp\ISLANDS_1591881090.txt"
    // INTO TABLE island FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'
    // (@col1,@col2,@col3,@col4,@col5,@col6,@col7) set id = @col1, x=@col2,y=@col3,islandType =
    // @col4, availableTown=@col5,plusRessource = @col6, minusRessource = @col7
    @AllArgsConstructor
    class IslandDto {
        private String id;
        private String x;
        private String y;
        private String islandType; //FIXME ?
        private String availableTown;
        private String plusRessources;
        private String minusRessources;

        public Long getId() {
            return Long.parseLong(id);
        }

        public Integer getX() {
            return Integer.valueOf(x);
        }

        public Integer getY() {
            return Integer.valueOf(y);
        }

        public Integer getIslandType() {
            return Integer.valueOf(islandType);
        }

        public Integer getAvailableTown() {
            return Integer.valueOf(availableTown);
        }

        public Ressources getPlusRessources() {
            return Ressources.valueOf(plusRessources.toUpperCase());
        }

        public Ressources getMinusRessources() {
            return Ressources.valueOf(minusRessources.toUpperCase());
        }
    }

    Function<String[], KillRankDto> killRanksTransformer = lines -> new KillRankDto(lines[0], lines[1], lines[2]);

    @Getter
    @AllArgsConstructor
    class KillRankDto implements Dto {
        private String rank;
        private String id;
        private String point;

        public Long getId() {
            return Long.parseLong(id);
        }

        public String getIdStr() {
            return id;
        }

        public Long getRank() {
            return Long.parseLong(rank);
        }

        public Long getPoint() {
            return Long.valueOf(point);
        }
    }
}
