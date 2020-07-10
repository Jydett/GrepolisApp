package fr.jydet.grepolis.dao.player.impl;

import fr.jydet.grepolis.dao.HibernateDao;
import fr.jydet.grepolis.dao.player.PlayerDao;
import fr.jydet.grepolis.model.Player;

public class HibernatePlayerDao extends HibernateDao<Long, Player> implements PlayerDao {

    public HibernatePlayerDao() {
        super(Player.class);
    }
}
