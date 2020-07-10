package fr.jydet.grepolis.dao;

import fr.jydet.grepolis.dao.alliance.impl.HibernateAllianceDao;
import fr.jydet.grepolis.dao.island.impl.HibernateIslandDao;
import fr.jydet.grepolis.dao.player.impl.HibernatePlayerDao;
import fr.jydet.grepolis.dao.stucture.IDao;
import fr.jydet.grepolis.model.Alliance;
import fr.jydet.grepolis.model.Identifiable;
import fr.jydet.grepolis.model.Island;
import fr.jydet.grepolis.model.Player;

import java.io.Serializable;
import java.util.HashMap;

public class DaoRegistry {

    private static HashMap<String, IDao<?, ?>> daoRegistry;

    static {
        daoRegistry = new HashMap<>();

        initRegistry();
    }

    private static void initRegistry() {
        register(Player.class, new HibernatePlayerDao());
        register(Alliance.class, new HibernateAllianceDao());
        register(Island.class, new HibernateIslandDao());
    }

    private static <Id extends Serializable, T extends Identifiable<Id>> void register(Class<T> clazz, IDao<Id, T> instance) {
        daoRegistry.put(clazz.getSimpleName(), instance);
    }

    public static <Id extends Serializable, T extends Identifiable<Id>, R extends IDao<Id, T>> R getDao(Class<T> clazz) {
        return (R) daoRegistry.get(clazz.getSimpleName());
    }
}
