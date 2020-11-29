package com.audiolib.persistance.repository;

import java.util.List;
import java.util.Optional;

import com.audiolib.persistance.model.Artist;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ArtistRepo extends PagingAndSortingRepository<Artist, Long> {
    Optional<Artist> findArtistById(Long id);
    Page<Artist> findAll(Pageable page);
    Page<Artist> findByNameIgnoreCase(String name, Pageable page);
    List<Artist> findByNameIgnoreCase(String name);
}

