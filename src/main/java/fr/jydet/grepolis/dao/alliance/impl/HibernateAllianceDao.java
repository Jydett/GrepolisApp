package fr.jydet.grepolis.dao.alliance.impl;

import fr.jydet.grepolis.dao.HibernateDao;
import fr.jydet.grepolis.dao.alliance.AllianceDao;
import fr.jydet.grepolis.model.Alliance;

public class HibernateAllianceDao extends HibernateDao<Long, Alliance> implements AllianceDao {

    public HibernateAllianceDao() {
        super(Alliance.class);
    }
}
