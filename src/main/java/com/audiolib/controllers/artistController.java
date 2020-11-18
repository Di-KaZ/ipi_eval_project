package com.audiolib.controllers;

import java.net.URI;

import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.service.ArtistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@CrossOrigin // Utiliser pour ce passer du SimpleCorsFilter
@RequestMapping(value = "/artists")
public class artistController {

    @Autowired
    private ArtistService artistService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Artist> showAllPage(@RequestParam("page") Integer page_num,
                                    @RequestParam("size") Integer size,
                                    @RequestParam("sortProperty") String sortProperty,
                                    @RequestParam("sortDirection") String sortDirection,
                                    @RequestParam(value = "name", required = false) String name) {

        Pageable page = PageRequest.of(page_num.intValue(), size.intValue(),
                        Sort.by(sortDirection.equals("ASC") ?
                        Order.asc(sortProperty) : Order.desc(sortProperty)));

        if (name == null) {
            return artistService.findAll(page);
        }
        return artistService.findByNameIgnoreCase(name, page);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable("id") Long id) {
        Artist artist = artistService.findArtistById(id);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artist);
    }

    @RequestMapping(method = RequestMethod.PUT, value="/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Artist> modifyArtistById(@PathVariable("id") Long id, @RequestBody Artist artist) {
        Artist modifiedArtist = artistService.update(id, artist);

        if (modifiedArtist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(modifiedArtist);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist createdArtist = artistService.create(artist);

        if (createdArtist == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/?name={name}").buildAndExpand(createdArtist.getName()).toUri();
            return ResponseEntity.created(uri).body(createdArtist);
        }
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable("id") Long id) {
        try {
            artistService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
