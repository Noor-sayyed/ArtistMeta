package org.example.artistmeta.repository;

import org.example.artistmeta.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    Optional<Artist> findByStagename(String stagename);

    @Query("SELECT a FROM Artist a ORDER BY a.createddate ASC, a.artistid ASC")
    List<Artist> findAllOrderedByCreatedDate(Pageable pageable);
}

