package com.audiolib.controllers;

import java.util.List;

import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.service.ArtistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/artist")
public class artistController {

    @Autowired
    ArtistService artistService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json",
                    value = "")
    public List<Artist> getAll(@RequestParam("page") Integer page) {
        return artistService.findAll(page);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json",
                    value = "/{name}")
    public List<Artist> getArtist(@PathVariable(value = "name") String name, @RequestParam("page") Integer page) {
        return artistService.findByNameIgnoreCase(name, page);
    }
}
