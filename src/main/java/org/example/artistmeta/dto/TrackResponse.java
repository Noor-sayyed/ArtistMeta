package org.example.artistmeta.dto;

import java.util.UUID;

public class TrackResponse {

    private UUID id;
    private String title;
    private String genre;
    private Integer lengthSeconds;
    private UUID artistId;
    private String artistName;

    public TrackResponse() {
    }

    public TrackResponse(UUID id, String title, String genre, Integer lengthSeconds, UUID artistId, String artistName) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.lengthSeconds = lengthSeconds;
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Integer getLengthSeconds() {
        return lengthSeconds;
    }

    public void setLengthSeconds(Integer lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }

    public UUID getArtistId() {
        return artistId;
    }

    public void setArtistId(UUID artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
