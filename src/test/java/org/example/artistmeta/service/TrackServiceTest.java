package org.example.artistmeta.service;

import org.example.artistmeta.dto.TrackResponse;
import org.example.artistmeta.entity.Artist;
import org.example.artistmeta.entity.Track;
import org.example.artistmeta.exception.ResourceNotFoundException;
import org.example.artistmeta.repository.ArtistRepository;
import org.example.artistmeta.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    private final ArtistRepository artistRepository = Mockito.mock(ArtistRepository.class);
    private final TrackRepository trackRepository = Mockito.mock(TrackRepository.class);
    private final TrackService trackService = new TrackService(trackRepository, artistRepository);

    @Test
    void returnsTracksForExistingStageName() {
        UUID artistId = UUID.randomUUID();
        Artist artist = new Artist();
        artist.setArtistid(artistId);
        artist.setStagename("prince");
        artist.setArtistname("Prince");

        Track track1 = buildTrack(artist, "Purple Rain", "Pop Rock", 245);
        Track track2 = buildTrack(artist, "1999", "Synth-Pop", 375);

        Pageable pageable = PageRequest.of(0, 20);
        Page<Track> trackPage = new PageImpl<>(List.of(track1, track2), pageable, 2);

        when(artistRepository.findByStagename("prince")).thenReturn(Optional.of(artist));
        when(trackRepository.findAllByArtist_Artistid(artistId, pageable)).thenReturn(trackPage);

        Page<TrackResponse> result = trackService.getTracksForStageName("prince", pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Purple Rain", result.getContent().get(0).getTitle());
        assertEquals("1999", result.getContent().get(1).getTitle());
        assertEquals(artistId, result.getContent().get(0).getArtistId());
    }

    @Test
    void throwsWhenStageNameDoesNotExist() {
        Pageable pageable = PageRequest.of(0, 20);
        when(artistRepository.findByStagename("unknown-artist")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> trackService.getTracksForStageName("unknown-artist", pageable));

        // Confirm it never even attempts a track lookup once the artist isn't found
        verify(trackRepository, never()).findAllByArtist_Artistid(any(UUID.class), any(Pageable.class));
    }

    @Test
    void returnsEmptyPageWhenArtistHasNoTracks() {
        UUID artistId = UUID.randomUUID();
        Artist artist = new Artist();
        artist.setArtistid(artistId);
        artist.setStagename("newartist");

        Pageable pageable = PageRequest.of(0, 20);
        Page<Track> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(artistRepository.findByStagename("newartist")).thenReturn(Optional.of(artist));
        when(trackRepository.findAllByArtist_Artistid(artistId, pageable)).thenReturn(emptyPage);

        Page<TrackResponse> result = trackService.getTracksForStageName("newartist", pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void passesThroughRequestedPageableUnchanged() {
        UUID artistId = UUID.randomUUID();
        Artist artist = new Artist();
        artist.setArtistid(artistId);
        artist.setStagename("someartist");

        Pageable secondPage = PageRequest.of(1, 5);
        Page<Track> trackPage = new PageImpl<>(List.of(), secondPage, 0);

        when(artistRepository.findByStagename("someartist")).thenReturn(Optional.of(artist));
        when(trackRepository.findAllByArtist_Artistid(artistId, secondPage)).thenReturn(trackPage);

        trackService.getTracksForStageName("someartist", secondPage);

        // Confirms the exact Pageable the caller supplied is what actually reaches the repository
        verify(trackRepository, times(1)).findAllByArtist_Artistid(artistId, secondPage);
    }

    private Track buildTrack(Artist artist, String title, String genre, int length) {
        Track track = new Track();
        track.setTrackid(UUID.randomUUID());
        track.setTitle(title);
        track.setGenre(genre);
        track.setLength(length);
        track.setArtist(artist);
        return track;
    }
}