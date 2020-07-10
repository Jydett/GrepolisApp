package fr.jydet.grepolis.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DownloadRestricter {
    public static final Duration downloadThreshold = Duration.of(1, ChronoUnit.DAYS);

    public static boolean check(CSVDownloader.DataType dataType) {
        try {
            return Files.list(new File("./").toPath())
                .map(Path :: toString)
                .map(s -> s.substring(2))
                .filter(p -> p.startsWith(dataType.name()))
                .filter(p -> p.endsWith(".txt"))
                .map(p -> {
                    final int lastIndexOf = p.lastIndexOf('_');
                    if (lastIndexOf == -1) {
                        return null;
                    }
                    return p.substring(p.lastIndexOf('_'), p.length() - 3);
                })
                .filter(Objects :: nonNull)
                .anyMatch(s ->
                    Duration.between(
                        Instant.ofEpochSecond(Long.parseLong(s)),
                        Instant.ofEpochSecond(System.currentTimeMillis())
                    ).compareTo(downloadThreshold) > 0
                );
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }
}
