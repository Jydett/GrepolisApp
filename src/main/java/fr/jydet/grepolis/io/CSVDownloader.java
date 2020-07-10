package fr.jydet.grepolis.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

public class CSVDownloader {

    public static final String SERVER_ID = "fr126";

    public static final String API_END_POINT = "https://" + SERVER_ID + ".grepolis.com/data/%s.txt.gz";

    public static Optional<String> download(DataType dataType) {
        if (DownloadRestricter.check(dataType)) {
            return Optional.empty();
        }
        try {
            URL website = new URL(String.format(API_END_POINT, dataType.name().toLowerCase()));
            String dest = "./" + dataType.name() + "_" + Instant.now().getEpochSecond() + ".txt";
            File destination = new File(dest + ".gz");
            System.out.println(website);
            FileUtils.copyURLToFile(website, destination, 0, 0);//FIXME infinite timeout
            unGunzipFile(destination, dest);
            destination.delete();
            return Optional.of(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void unGunzipFile(File compressedFile, String decompressedFile) {
        byte[] buffer = new byte[1024];

        try {

            FileInputStream fileIn = new FileInputStream(compressedFile);

            GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);

            FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);

            int bytes_read;

            while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {

                fileOutputStream.write(buffer, 0, bytes_read);
            }

            gZIPInputStream.close();
            fileOutputStream.close();

            System.out.println("The file was decompressed successfully!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public enum DataType {
        PLAYERS,
        ALLIANCES,
        TOWNS,
        ISLANDS,
        PLAYER_KILLS_ALL,
        PLAYER_KILLS_ATT,
        PLAYER_KILLS_DEF,
        ALLIANCE_KILLS_ALL,
        ALLIANCE_KILLS_ATT,
        ALLIANCE_KILLS_DEF,
        CONQUERS
    }
}
