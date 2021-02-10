package com.audiolib.restControllers;

import com.audiolib.persistance.model.Album;
import com.audiolib.persistance.service.AlbumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin // Utiliser pour ce passer du SimpleCorsFilter
@RequestMapping(value = "/albums")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable("id") Long id) {
        try {
            albumService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Album> postAlbum(@RequestBody Album album) {
        Album createdAlbum = albumService.create(album);

        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createdAlbum);
    }
}