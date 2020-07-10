package fr.jydet.grepolis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KillsRank {
    private long rank;
    private long point;
}
