package com.audiolib.persistance.service;

import java.util.List;
import java.util.Optional;

import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.model.ArtistDTO;
import com.audiolib.persistance.repository.ArtistRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepo artistRepo;

    public Optional<Artist> findById(Long id) {
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
        if (!artist.getName().isEmpty() || artistRepo.findByNameIgnoreCase(artist.getName()).isPresent()) {
            return null;
        }
        return artistRepo.save(artist);
    }

    public Artist create(ArtistDTO artistDTO) {
        // vertif si un artiste avec ce nom n'existe pas deja ou si le nom est vide
        if (artistDTO.getName().isEmpty() || artistRepo.findByNameIgnoreCase(artistDTO.getName()).isPresent()) {
            return null;
        }
        // nouvel artiste
        if (artistDTO.getId() == null) {
            Artist artist = new Artist();
            artist.setName(artistDTO.getName());
            return artist;
        } else { // on update un artiste existant
            Optional<Artist> artist = artistRepo.findById(artistDTO.getId());
            if (artist.isPresent()) {
                artist.get().setName(artistDTO.getName());
                artistRepo.save(artist.get());
                return artist.get();
            }
        }
        return null;
    }

    public Artist update(Artist artist) {
        if (artist != null) {
            Optional<Artist> toModify = artistRepo.findArtistById(artist.getId());
            if (!toModify.isPresent())
                return null;
            toModify.get().setName(artist.getName());
            toModify.get().setAlbums(artist.getAlbums());
            return artistRepo.save(toModify.get());
        }
        return null;
    }

    public void delete(Long id) {
        artistRepo.deleteById(id);
    }

    public void delete(Artist artist) {
        if (artist != null) {
            artistRepo.delete(artist);
        }
    }
}
