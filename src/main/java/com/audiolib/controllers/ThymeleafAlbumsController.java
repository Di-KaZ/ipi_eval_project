package com.audiolib.controllers;

import com.audiolib.persistance.model.Album;
import com.audiolib.persistance.model.AlbumDTO;
import com.audiolib.persistance.service.AlbumService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafAlbumsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThymeleafAlbumsController.class);

    @Autowired
    AlbumService albumService;

    @DeleteMapping(value = "albums", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView deleteAlbum(AlbumDTO albumDTO, RedirectAttributes redirectAttributes) {
        LOGGER.info("Album DTO recup DELETE : {}", albumDTO);
        if (albumDTO == null) {
            redirectAttributes.addFlashAttribute("message", "L'album n'existe pas !");
            return new RedirectView("/thymeleaf/artists/");
        }
        try {
            albumService.delete(albumDTO.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    String.format("La supression de l'album '%s' n'a pas réussi !", albumDTO.getTitle()));
            // redirection sur l'artiste d'ou vien l'album
            new RedirectView("" + albumDTO.getArtistId().toString());
        }
        redirectAttributes.addFlashAttribute("message",
                String.format("La supression de l'album '%s' a réussi !", albumDTO.getTitle()));
        // redirection sur l'artiste d'ou vien l'album
        return new RedirectView("/thymeleaf/artists/" + albumDTO.getArtistId().toString());
    }

    @PostMapping(value = "albums", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createSaveAlbum(AlbumDTO albumDTO, RedirectAttributes redirectAttributes) {
        Album album;
        LOGGER.info("Album DTO recup POST : {}", albumDTO);
        album = albumService.create(albumDTO);
        if (album == null) {
            redirectAttributes.addFlashAttribute("message",
                    "une erreur est survenu lors de la modification ou la creation de l'album");
        }
        redirectAttributes.addFlashAttribute("message", "Tout c'est bien passé !");
        return new RedirectView("/thymeleaf/artists/" + albumDTO.getArtistId().toString());
    }
}
