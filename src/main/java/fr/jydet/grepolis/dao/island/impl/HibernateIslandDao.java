package fr.jydet.grepolis.dao.island.impl;

import fr.jydet.grepolis.dao.HibernateDao;
import fr.jydet.grepolis.dao.island.IslandDao;
import fr.jydet.grepolis.model.Island;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;

public class HibernateIslandDao extends HibernateDao<Long, Island> implements IslandDao {

    public HibernateIslandDao() {
        super(Island.class);
    }

    @Override
    public void saveFast(File file) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.createNativeQuery(String.format("LOAD DATA INFILE \"%s\" " +
            "INTO TABLE island FIELDS TERMINATED BY ',' LINES TERMINATED BY '\\n' " +
            "(@col1,@col2,@col3,@col4,@col5,@col6,@col7) set id = @col1, x=@col2,y=@col3,islandType = " +
            "@col4, availableTown=@col5,plusRessource = @col6, minusRessource = @col7", file.getAbsolutePath().replaceAll("\\\\", "/")))
            .executeUpdate();
        transaction.commit();
    }
}
