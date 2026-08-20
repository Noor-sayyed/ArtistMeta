package org.example.artistmeta.repository;

import org.example.artistmeta.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrackRepository extends JpaRepository<Track, UUID> {
    Page<Track> findAllByArtist_Artistid(UUID artistId, Pageable pageable);
}
