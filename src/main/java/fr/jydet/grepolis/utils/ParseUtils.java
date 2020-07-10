package fr.jydet.grepolis.utils;

public class ParseUtils {

    public static Long asLongOrEmpty(String toParse) {
        if (toParse.isEmpty()) {
            return 0L;
        } else {
            return Long.parseLong(toParse);
        }
    }
}
