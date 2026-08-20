package org.example.artistmeta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "track")
public class Track {

    @Id
    @Column(name = "trackid", nullable = false)
    private UUID trackid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "length")
    private Integer length;

    @ManyToOne
    @JoinColumn(name = "artistid", referencedColumnName = "artistid", nullable = false)
    private Artist artist;

    @Column(name = "updatedby")
    private UUID updatedby;

    @Column(name = "updatedtimestamp", nullable = false)
    private LocalDateTime updatedtimestamp;

    public Track() {
    }

    public UUID getTrackid() {
        return trackid;
    }

    public void setTrackid(UUID trackid) {
        this.trackid = trackid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
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
}
