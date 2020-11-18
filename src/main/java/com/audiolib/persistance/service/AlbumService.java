package com.audiolib.persistance.service;

import java.util.Optional;

import com.audiolib.persistance.model.Album;
import com.audiolib.persistance.repository.AlbumRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    @Autowired
    AlbumRepo albumRepo;

    public Optional<Album> findById(Long id) {
        return albumRepo.findById(id);
    }

    public void delete(Long id) {
        albumRepo.deleteById(id);
    }

    public Album create(Album album) {
        return albumRepo.save(album);
    }
}
