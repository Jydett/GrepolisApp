package fr.jydet.grepolis.dao.alliance;

import fr.jydet.grepolis.model.Alliance;

public interface AllianceDao {
    void save(Alliance alliance);

    Alliance proxy(Long allianceId);

    long count();
}
