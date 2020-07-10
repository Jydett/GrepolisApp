package fr.jydet.grepolis.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Conquest {

    @Id
    private long id;//FIXME loul enleve ca

    //FIXME id = les 2
    @ManyToOne
    private Town town;

    @Basic
    private LocalDateTime localDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player looser;
    @ManyToOne(fetch = FetchType.LAZY)
    private Player winner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Alliance looserAlliance;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alliance winnerAlliance;

    private int townPoint;
}
