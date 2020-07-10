package fr.jydet.grepolis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Player implements Identifiable<Long>, Displayable {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    private Alliance alliance;
    private long points;
    private int rank;

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

    //FIXME on verra
    private int townCount;

    private final static List<String> HEADERS = Arrays.asList("id", "name", "allianceName", "points", "rank", "townCount");

    @Override
    public List<String> getHeaders() {
        return HEADERS;
    }

    @Override
    public List<String> getRow() {
        return Arrays.asList(
            id.toString(),
            name,
            Optional.of(alliance).map(a -> alliance.getName()).orElse(""),
            Objects.toString(points),
            Objects.toString(rank),
            Objects.toString(townCount)
        );
    }
}
