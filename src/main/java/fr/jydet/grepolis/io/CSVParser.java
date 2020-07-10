package fr.jydet.grepolis.io;

import fr.jydet.grepolis.io.dto.Dto;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CSVParser {

    public static final String SEPARATOR = ",";

    public static <O> List<O> read(String file, Function<String[], O> resultTransformer) {
        List<O> res = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                res.add(resultTransformer.apply(decodeValue(line).split(SEPARATOR)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
