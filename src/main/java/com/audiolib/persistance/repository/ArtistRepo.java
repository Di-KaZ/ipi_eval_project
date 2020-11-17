package com.audiolib.persistance.repository;

import java.util.List;

import com.audiolib.persistance.model.Artist;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArtistRepo extends PagingAndSortingRepository<Artist, Long> {
    Artist findArtistById(Long id);

    List<Artist> findByName(String name);
}

