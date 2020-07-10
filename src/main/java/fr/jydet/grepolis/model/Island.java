package fr.jydet.grepolis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Island implements Identifiable<Long> {
    @Id
    private Long id;
    private Integer x;
    private Integer y;
    private Integer islandType; //FIXME ?
    private Integer availableTown;

    @Enumerated(EnumType.STRING)
    private Ressources plusRessource;

    @Enumerated(EnumType.STRING)
    private Ressources minusRessource;
}
