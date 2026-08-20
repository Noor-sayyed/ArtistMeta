package org.example.artistmeta.service;

import org.example.artistmeta.dto.ArtistResponse;
import org.example.artistmeta.entity.Artist;
import org.example.artistmeta.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Test
    void rotatesFairlyAcrossFullCycle() {
        int artistCount = 10;
        List<Artist> artists = buildFakeArtists(artistCount);

        ArtistRepository artistRepository = Mockito.mock(ArtistRepository.class);
        when(artistRepository.count()).thenReturn((long) artistCount);

        // Simulate the real OFFSET/LIMIT behavior your @Query relies on:
        // whichever page number is requested, return that single artist.
        when(artistRepository.findAllOrderedByCreatedDate(any(Pageable.class)))
                .thenAnswer(invocation -> {
                    Pageable pageable = invocation.getArgument(0);
                    int index = pageable.getPageNumber();
                    return List.of(artists.get(index));
                });

        Instant start = Instant.parse("2026-01-01T00:00:00Z");
        Set<UUID> seenInFirstCycle = new HashSet<>();
        UUID firstArtistId = null;

        // Walk two full cycles (2 * artistCount days) to prove both
        // "everyone appears exactly once" AND "it wraps correctly"
        for (int day = 0; day < artistCount * 2; day++) {
            Clock testClock = Clock.fixed(start.plus(Duration.ofDays(day)), ZoneOffset.UTC);
            ArtistService service = new ArtistService(artistRepository, testClock);

            ArtistResponse todaysArtist = service.getArtistOfTheDay();

            if (day == 0) {
                firstArtistId = todaysArtist.getArtistId();
            }

            if (day < artistCount) {
                // First cycle: every artist must be distinct - no repeats, no skips
                boolean isNewArtist = seenInFirstCycle.add(todaysArtist.getArtistId());
                assertEquals(true, isNewArtist,
                        "Artist repeated within a single cycle on day " + day);
            }

            if (day == artistCount) {
                // Day N (right after the last artist) must restart with the first artist
                assertEquals(firstArtistId, todaysArtist.getArtistId(),
                        "Cycle did not restart with the first artist on day " + artistCount);
            }
        }

        // Confirm the full first cycle covered every single artist, no gaps
        assertEquals(artistCount, seenInFirstCycle.size(),
                "Not every artist appeared during the first full cycle");
    }

    private List<Artist> buildFakeArtists(int count) {
        List<Artist> artists = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Artist artist = new Artist();
            artist.setArtistid(UUID.randomUUID());
            artist.setStagename("artist" + i);
            artist.setArtistname("Artist " + i);
            artists.add(artist);
        }
        return artists;
    }
}