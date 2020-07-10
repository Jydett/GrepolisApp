package fr.jydet.grepolis.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Town {
    @Id
    private long id;
    @ManyToOne
    private Player player;
    private String name;

    @ManyToOne
    private Island island;
    private int numberOnIsland;

    private long points;
}
