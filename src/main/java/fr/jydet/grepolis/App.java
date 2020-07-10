package fr.jydet.grepolis;

import fr.jydet.grepolis.dao.alliance.impl.HibernateAllianceDao;
import fr.jydet.grepolis.dao.island.impl.HibernateIslandDao;
import fr.jydet.grepolis.dao.player.impl.HibernatePlayerDao;
import fr.jydet.grepolis.io.CSVDownloader;
import fr.jydet.grepolis.io.CSVParser;
import fr.jydet.grepolis.io.CSVToDatabase;
import fr.jydet.grepolis.io.dto.Dto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(4);
        final CSVDownloader.DataType[] dataTypes = {
            CSVDownloader.DataType.ALLIANCES,
            CSVDownloader.DataType.ALLIANCE_KILLS_ALL,
            CSVDownloader.DataType.ALLIANCE_KILLS_ATT,
            CSVDownloader.DataType.ALLIANCE_KILLS_DEF,
            CSVDownloader.DataType.PLAYERS,
            CSVDownloader.DataType.PLAYER_KILLS_ALL,
            CSVDownloader.DataType.PLAYER_KILLS_ATT,
            CSVDownloader.DataType.PLAYER_KILLS_DEF,
        };
        for (CSVDownloader.DataType dataType : dataTypes) {
            service.submit(() -> {
                CSVDownloader.download(dataType);
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Instant start = Instant.now();
        new CSVToDatabase(new HibernateAllianceDao(), new HibernatePlayerDao(), new HibernateIslandDao())
            .saveAlliances(
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.ALLIANCES), Dto.allianceTransformer),
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.ALLIANCE_KILLS_ALL), Dto.killRanksTransformer),
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.ALLIANCE_KILLS_ATT), Dto.killRanksTransformer),
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.ALLIANCE_KILLS_DEF), Dto.killRanksTransformer)
            )
            .savePlayers(
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.PLAYERS), Dto.playerTransformer),
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.PLAYER_KILLS_ALL), Dto.killRanksTransformer),
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.PLAYER_KILLS_ATT), Dto.killRanksTransformer),
                CSVParser.read(findLatestFileOfType(CSVDownloader.DataType.PLAYER_KILLS_DEF), Dto.killRanksTransformer)
            )
//            .saveIslandsFast("ISLANDS_1591881090.txt")
//            .saveIslands(
//                CSVParser.read("ISLANDS_1591881090.txt", Dto.islandsTransformer)
//            )
        ;
        System.out.println(String.format("Operation completed in %d ms", Duration.between(start, Instant.now()).toMillis()));
    }

    public static String findLatestFileOfType(CSVDownloader.DataType dataType) {
        try {
            return dataType.name() + "_" + Files.list(new File("").toPath())
                .map(Path :: toString)
                .filter(p -> p.startsWith(dataType.name()))
                .map(p -> p.substring(dataType.name().length() + 1, p.length() - 4))
                .mapToInt(Integer :: parseInt).max().orElse(-1) + ".txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
