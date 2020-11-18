package com.audiolib.persistance.repository;


import java.util.Optional;

import com.audiolib.persistance.model.Album;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepo extends CrudRepository<Album, Long> {
    Optional<Album> findById(Long id);
}
