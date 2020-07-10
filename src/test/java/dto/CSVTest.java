package dto;

import fr.jydet.grepolis.io.CSVDownloader;
import fr.jydet.grepolis.io.CSVParser;
import fr.jydet.grepolis.io.dto.Dto;
import fr.jydet.grepolis.utils.ConsoleLoadingBar;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CSVTest {

    @Test
    public void testCSV() {
        String file = "K:\\save arthur\\Arthur\\geopolis\\players.txt";
        List<Dto.PlayerDto> players = CSVParser.read(file, Dto.playerTransformer);
        System.out.println(players.size());
    }

    @Test
    public void testDownload() {
        for (CSVDownloader.DataType value : CSVDownloader.DataType.values()) {
            CSVDownloader.download(value);
        }
    }

    @Test
    public void loadingBarTest() {
        ConsoleLoadingBar.printPercentWithETA(null, 60, 60, Instant.now().minus(7, ChronoUnit.MINUTES));
        ConsoleLoadingBar.printPercentWithETA(null, 1, 40, Instant.now().minus(1, ChronoUnit.MINUTES));
        ConsoleLoadingBar.printPercentWithETA(null, 1, 10, Instant.now().minus(1, ChronoUnit.MINUTES));
        ConsoleLoadingBar.printPercentWithETA(null, 1, 100, Instant.now().minus(1, ChronoUnit.MINUTES));
        ConsoleLoadingBar.printPercentWithETA(null, 1, 1000, Instant.now().minus(1, ChronoUnit.MINUTES));
    }
}
