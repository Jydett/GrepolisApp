package fr.jydet.grepolis.dao.island;

import fr.jydet.grepolis.model.Island;

import java.io.File;

public interface IslandDao {
    void save(Island island);

    void saveFast(File file);

    long count();
}
