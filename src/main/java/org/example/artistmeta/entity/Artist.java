package org.example.artistmeta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @Column(name = "artistid", nullable = false)
    private UUID artistid;

    @Column(name = "stagename", nullable = false, unique = true)
    private String stagename;

    @Column(name = "artistname", nullable = false)
    private String artistname;

    @Column(name = "alias")
    private String alias;

    @Column(name = "version")
    private BigDecimal version;

    @Column(name = "updatedby")
    private UUID updatedby;

    @Column(name = "updatedtimestamp", nullable = false)
    private LocalDateTime updatedtimestamp;

    @Column(name = "createddate")
    private LocalDate createddate;

    @Column(name = "sequenceno", unique = true)
    private Integer sequenceno;

    // constructors, getters, setters

    public Artist() {
    }

    public UUID getArtistid() {
        return artistid;
    }

    public void setArtistid(UUID artistid) {
        this.artistid = artistid;
    }

    public String getStagename() {
        return stagename;
    }

    public void setStagename(String stagename) {
        this.stagename = stagename;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public UUID getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(UUID updatedby) {
        this.updatedby = updatedby;
    }

    public LocalDateTime getUpdatedtimestamp() {
        return updatedtimestamp;
    }

    public void setUpdatedtimestamp(LocalDateTime updatedtimestamp) {
        this.updatedtimestamp = updatedtimestamp;
    }

    public LocalDate getCreateddate() {
        return createddate;
    }

    public void setCreateddate(LocalDate createddate) {
        this.createddate = createddate;
    }

    public Integer getSequenceno() {
        return sequenceno;
    }

    public void setSequenceno(Integer sequenceno) {
        this.sequenceno = sequenceno;
    }
}
