package org.example.artistmeta.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateArtistNameRequest {

    @NotBlank
    private String stagename;

    @NotBlank
    private String artistname;

    public UpdateArtistNameRequest() {
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
}

