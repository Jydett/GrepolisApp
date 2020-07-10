package fr.jydet.grepolis.model;

import java.util.List;

public interface Displayable {
    List<String> getHeaders();

    List<String> getRow();
}
