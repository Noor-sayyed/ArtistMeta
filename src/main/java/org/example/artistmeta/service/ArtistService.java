package org.example.artistmeta.service;

import org.example.artistmeta.dto.ArtistResponse;
import org.example.artistmeta.dto.UpdateArtistNameRequest;
import org.example.artistmeta.entity.Artist;
import org.example.artistmeta.exception.ResourceNotFoundException;
import org.example.artistmeta.repository.ArtistRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import java.time.Clock;


@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final Clock clock;

    public ArtistService(ArtistRepository artistRepository, Clock clock) {
        this.artistRepository = artistRepository;
        this.clock = clock;

    }

    @Transactional
    public ArtistResponse updateArtistName(UpdateArtistNameRequest request) {
        Artist artist = artistRepository.findByStagename(request.getStagename())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found: " + request.getStagename()));

        String currentArtistName = artist.getArtistname();
        String requestedArtistName = request.getArtistname();

        if (!currentArtistName.equals(requestedArtistName)) {
            String currentAlias = artist.getAlias();
            if (currentAlias == null || currentAlias.isBlank()) {
                artist.setAlias(currentArtistName);
            } else {
                artist.setAlias(currentAlias + ", " + currentArtistName);
            }

            artist.setArtistname(requestedArtistName);
            artist.setUpdatedtimestamp(LocalDateTime.now());
        }

        Artist saved = artistRepository.save(artist);
        return new ArtistResponse(saved.getArtistid(), saved.getStagename(), saved.getArtistname(), saved.getAlias());
    }

    public ArtistResponse getArtistOfTheDay() {
        long totalArtists = artistRepository.count();
        if (totalArtists == 0) {
            throw new ResourceNotFoundException("No artists available");
        }

        long todaysIndex = LocalDate.now(clock).toEpochDay() % totalArtists;

        Pageable singleResult = PageRequest.of((int) todaysIndex, 1);
        List<Artist> result = artistRepository.findAllOrderedByCreatedDate(singleResult);
        Artist artist = result.get(0);

        return new ArtistResponse(
                artist.getArtistid(),
                artist.getStagename(),
                artist.getArtistname(),
                artist.getAlias()
        );
    }
}

