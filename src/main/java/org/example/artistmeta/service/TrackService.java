package org.example.artistmeta.service;

import org.example.artistmeta.dto.CreateTrackRequest;
import org.example.artistmeta.dto.TrackResponse;
import org.example.artistmeta.entity.Artist;
import org.example.artistmeta.entity.Track;
import org.example.artistmeta.exception.ResourceNotFoundException;
import org.example.artistmeta.repository.ArtistRepository;
import org.example.artistmeta.repository.TrackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;

    public TrackService(TrackRepository trackRepository, ArtistRepository artistRepository) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
    }

    @Transactional
    public TrackResponse addTrack(CreateTrackRequest req) {
        // resolve artist by stagename from request, then use artistid relation for track insert
        Artist artist = artistRepository.findByStagename(req.getStagename())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found: " + req.getStagename()));

        Track track = new Track();
        track.setTrackid(UUID.randomUUID());
        track.setTitle(req.getTitle());
        track.setGenre(req.getGenre());
        track.setLength(req.getLengthSeconds());
        track.setArtist(artist);
        track.setUpdatedtimestamp(LocalDateTime.now());

        Track saved = trackRepository.save(track);

        return new TrackResponse(saved.getTrackid(), saved.getTitle(), saved.getGenre(), saved.getLength(), artist.getArtistid(), artist.getStagename());
    }

    // Fetch paginated tracks by stage name, resolving the artist internally before querying tracks
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<org.example.artistmeta.dto.TrackResponse> getTracksForStageName(String stageName, org.springframework.data.domain.Pageable pageable) {
        Artist artist = artistRepository.findByStagename(stageName)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found: " + stageName));

        var page = trackRepository.findAllByArtist_Artistid(artist.getArtistid(), pageable);
        return page.map(t -> new org.example.artistmeta.dto.TrackResponse(
                t.getTrackid(), t.getTitle(), t.getGenre(), t.getLength(), artist.getArtistid(), artist.getStagename()
        ));
    }
}
