package fr.jydet.grepolis.dao.player;

import fr.jydet.grepolis.dao.stucture.Page;
import fr.jydet.grepolis.dao.stucture.PageRequest;
import fr.jydet.grepolis.dao.stucture.Pageable;
import fr.jydet.grepolis.dao.stucture.Specification;
import fr.jydet.grepolis.model.Player;

import java.util.Collection;
import java.util.List;

public interface PlayerDao {
    void save(Player player);

    long count();

    Page<Player> findAll(Specification<Player> specification);

    Page<Player> findAll(Specification<Player> parse, Pageable pageable);

    void saveAll(Collection<Player> players);
}
