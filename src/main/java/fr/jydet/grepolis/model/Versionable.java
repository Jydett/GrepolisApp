package fr.jydet.grepolis.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.time.LocalDateTime;

public abstract class Versionable {

    @Column(columnDefinition = "TIMESTAMP(6)")
    @Basic
    public LocalDateTime startTimestamp;

    @Column(columnDefinition = "TIMESTAMP(6)")
    @Basic
    public LocalDateTime end_timestamp;

}
