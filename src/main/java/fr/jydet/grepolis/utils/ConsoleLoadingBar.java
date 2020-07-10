package fr.jydet.grepolis.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ConsoleLoadingBar {
    public static void printPercent(String notice, int current, int max) {
        int progress = (int)(current*100)/max;
        StringBuffer sb = new StringBuffer();
        int i;
        for (i=0; i < (progress/5); i++){
            sb.append("#");
        }
        while (i < 20) {
            sb.append(".");
            i++;
        }
        if (notice != null) {
            System.out.println('\r' + notice);
        }
        System.out.print("\r[" + sb + "] (" + progress + "%) " +current + "/" + max);
    }

    public static void printPercentWithETA(String notice, int current, int max, Instant begin) {
        int progress = (int)(current*100)/max;
        StringBuffer sb = new StringBuffer();
        int i;
        for (i=0; i < (progress/5); i++){
            sb.append("#");
        }
        while (i < 20) {
            sb.append(".");
            i++;
        }
        if (notice != null) {
            System.out.println('\r' + notice);
        }
        String eta;
        if (current == 0) {
            eta = "???";
        } else {
            double v = ((double) Duration.between(begin, Instant.now()).getSeconds()) * (max - current) / (double) current;
            eta = humanReadableFormat(Duration.of((long) v, ChronoUnit.SECONDS));
        }
        System.out.print("\r[" + sb + "] (" + progress + "%) " +current + "/" + max + " " + eta);
    }

    private static String humanReadableFormat(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

}
