package org.example.artistmeta.controller;

import jakarta.validation.Valid;
import org.example.artistmeta.dto.ArtistResponse;
import org.example.artistmeta.dto.TrackResponse;
import org.example.artistmeta.dto.UpdateArtistNameRequest;
import org.example.artistmeta.service.ArtistService;
import org.example.artistmeta.service.TrackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final TrackService trackService;
    private final ArtistService artistService;

    public ArtistController(TrackService trackService, ArtistService artistService) {
        this.trackService = trackService;
        this.artistService = artistService;
    }

    @GetMapping("/name/{stageName}/tracks")
    public ResponseEntity<Page<TrackResponse>> getTracksByStageName(
            @PathVariable("stageName") String stageName,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        int safeSize = Math.min(Math.max(1, size), 100);
        Pageable pageable = PageRequest.of(Math.max(0, page), safeSize);
        Page<TrackResponse> res = trackService.getTracksForStageName(stageName, pageable);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/name/update")
    public ResponseEntity<ArtistResponse> updateArtistName(@Valid @RequestBody UpdateArtistNameRequest request) {
        ArtistResponse response = artistService.updateArtistName(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/of-the-day")
    public ResponseEntity<ArtistResponse> getArtistOfTheDay() {
        return ResponseEntity.ok(artistService.getArtistOfTheDay());
    }


}
