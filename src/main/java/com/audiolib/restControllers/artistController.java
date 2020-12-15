package com.audiolib.restControllers;

import java.net.URI;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@CrossOrigin // Utilisé pour ce passer du SimpleCorsFilter
@RequestMapping(value = "/artists")
public class artistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public Page<Artist> showAllPage(@RequestParam("page") Integer pageNum,
                                    @RequestParam("size") Integer size,
                                    @RequestParam("sortProperty") String sortProperty,
                                    @RequestParam("sortDirection") String sortDirection,
                                    @RequestParam(value = "name", required = false) String name) {

        Pageable page = PageRequest.of(pageNum.intValue(), size.intValue(),
                        Sort.by(sortDirection.equalsIgnoreCase("ASC") ?
                        Order.asc(sortProperty) : Order.desc(sortProperty)));

        if (name == null) {
            return artistService.findAll(page);
        }
        return artistService.findByNameIgnoreCase(name, page);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable("id") Long id) {
        Optional<Artist> artist = artistService.findById(id);
        if (!artist.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artist.get());
    }

    @PutMapping(value="/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Artist> modifyArtistById(@PathVariable("id") Long id, @RequestBody Artist artist) {
        Artist modifiedArtist = artistService.update(artist);

        if (modifiedArtist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(modifiedArtist);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist createdArtist = artistService.create(artist);

        if (createdArtist == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/?id={id}").buildAndExpand(createdArtist.getId()).toUri();
            return ResponseEntity.created(uri).body(createdArtist);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable("id") Long id) {
        try {
            artistService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
