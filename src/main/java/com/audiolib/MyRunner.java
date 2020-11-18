package com.audiolib;

import com.audiolib.persistance.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    ArtistService artistService;

    @Override
    public void run(String... strings) throws Exception {
        // Iterable<Artist> artists = artistService.findAll();

        // for (Artist artist : artists) {
        //     Set<Album> albums = artist.getAlbums();
        //     print(artist.getName());
        //     for (Album album : albums) {
        //         print(album.getTitle());
        //     }
        // }
    }

    public static void print(Object t) {
        System.out.println(t);
    }
}
