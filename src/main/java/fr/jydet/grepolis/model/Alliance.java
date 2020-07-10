package fr.jydet.grepolis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alliance implements Identifiable<Long> {
    @Id
    private Long id;
    private String name;
    private Long points;
    private Integer villageCount; //FIXME ??
    private Integer membersCount;
    private Long rank;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "point", column = @Column(name = "allKillPoint")),
            @AttributeOverride(name = "rank", column = @Column(name = "allKillRank"))
    })
    private KillsRank allKillsRank;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "point", column = @Column(name = "defKillPoint")),
            @AttributeOverride(name = "rank", column = @Column(name = "defKillRank"))
    })
    private KillsRank defKillsRank;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "point", column = @Column(name = "attackKillPoint")),
            @AttributeOverride(name = "rank", column = @Column(name = "attackKillRank"))
    })
    private KillsRank attackKillsRank;
}
