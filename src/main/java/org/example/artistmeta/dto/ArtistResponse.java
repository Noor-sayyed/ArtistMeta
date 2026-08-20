package org.example.artistmeta.dto;

import java.util.UUID;

public class ArtistResponse {

    private UUID artistId;
    private String stagename;
    private String artistname;
    private String alias;

    public ArtistResponse() {
    }

    public ArtistResponse(UUID artistId, String stagename, String artistname, String alias) {
        this.artistId = artistId;
        this.stagename = stagename;
        this.artistname = artistname;
        this.alias = alias;
    }

    public UUID getArtistId() {
        return artistId;
    }

    public void setArtistId(UUID artistId) {
        this.artistId = artistId;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

