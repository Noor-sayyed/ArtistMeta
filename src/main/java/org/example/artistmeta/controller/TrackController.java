package org.example.artistmeta.controller;

import org.example.artistmeta.dto.CreateTrackRequest;
import org.example.artistmeta.dto.TrackResponse;
import org.example.artistmeta.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping
    public ResponseEntity<TrackResponse> createTrack(@Valid @RequestBody CreateTrackRequest req) {
        TrackResponse res = trackService.addTrack(req);
        return ResponseEntity.created(URI.create("/tracks/" + res.getId())).body(res);
    }
}
