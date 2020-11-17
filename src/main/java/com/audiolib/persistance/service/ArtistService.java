package com.audiolib.persistance.service;

import java.util.List;

import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.repository.ArtistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {
    @Autowired
    ArtistRepo artistRepo;

    public Artist findArtistById(Long id) {
        return artistRepo.findArtistById(id);
    }

    public List<Artist> findAll(Integer page_num) {
        Pageable page = PageRequest.of(page_num.intValue(), 20);
        return artistRepo.findAll(page).getContent();
    }

    public List<Artist> findByNameIgnoreCase(String name, Integer page_num) {
        Pageable page = PageRequest.of(page_num.intValue(), 20);
        return artistRepo.findByNameIgnoreCase(name, page).getContent();
    }
}
