package com.audiolib.persistance.service;

import java.util.List;
import java.util.Optional;

import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.repository.ArtistRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepo artistRepo;

    public Optional<Artist> findArtistById(Long id) {
        return artistRepo.findArtistById(id);
    }

    public Page<Artist> findByNameIgnoreCase(String name, Pageable page) {
        return artistRepo.findByNameContainingIgnoreCase(name, page);
    }

    public List<Artist> findByNameContainingIgnoreCase(String name) {
        return artistRepo.findByNameContainingIgnoreCase(name);
    }

    public Page<Artist> findAll(Pageable page) {
        return artistRepo.findAll(page);
    }

    public Artist create(Artist artist) {
        if (!artistRepo.findByNameContainingIgnoreCase(artist.getName()).isEmpty()) {
            return null;
        }
        return artistRepo.save(artist);
    }

    public Artist update(Long id, Artist artist) {
        Optional<Artist> toModify = artistRepo.findArtistById(id);
        if (!toModify.isPresent())
            return null;
        toModify.get().setName(artist.getName());
        toModify.get().setAlbums(artist.getAlbums());
        return artistRepo.save(toModify.get());
    }

    public void delete(Long id) {
        Optional<Artist> toDelete = artistRepo.findArtistById(id);

        if (toDelete.isPresent())
            return;
        artistRepo.delete(toDelete.get());
    }
}
