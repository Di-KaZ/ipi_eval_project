package com.audiolib.persistance.service;

import java.util.Optional;

import com.audiolib.persistance.model.Album;
import com.audiolib.persistance.model.AlbumDTO;
import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.repository.AlbumRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumService.class);

    @Autowired
    AlbumRepo albumRepo;

    @Autowired
    ArtistService artistService;

    public Optional<Album> findById(Long id) {
        return albumRepo.findById(id);
    }

    public void delete(Long id) {
        if (albumRepo.existsById(id)) {
            albumRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void delete(Album album) {
        if (album != null && albumRepo.existsById(album.getId())) {
            albumRepo.delete(album);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Album create(Album album) {
        return albumRepo.save(album);
    }

    public Album create(AlbumDTO albumDTO) {
        // si id est null on crée un nouvel album
        if (albumDTO.getTitle().isEmpty()) {
            return null;
        }
        if (albumDTO.getId() == null) {
            LOGGER.info("creation d'un nouvel album via le dataObject");
            Album newAlbum = new Album();
            Optional<Artist> artist = artistService.findById(albumDTO.getArtistId());

            newAlbum.setTitle(albumDTO.getTitle());
            if (artist.isPresent()) {
                newAlbum.setArtist(artist.get());
                return albumRepo.save(newAlbum);
            } else { // erreur
                return null;
            }
        }
        // on met a jour un existant
        Optional<Album> album = albumRepo.findById(albumDTO.getId());

        if (album.isPresent()) {
            LOGGER.info("Mise a jour d'un album existant via le dataObject");
            album.get().setTitle(albumDTO.getTitle());
            return albumRepo.save(album.get());
        }
        return null;
    }
}
