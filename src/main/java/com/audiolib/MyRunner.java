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
        print(artistService.findArtistById(1L).getName());
    }

    public static void print(Object t) {
        System.out.println(t);
    }
}
