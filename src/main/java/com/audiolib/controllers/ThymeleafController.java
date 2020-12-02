package com.audiolib.controllers;

import java.util.Optional;

import com.audiolib.persistance.model.Album;
import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.service.AlbumService;
import com.audiolib.persistance.service.ArtistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping(value = "/thymeleaf", method = RequestMethod.GET)
public class ThymeleafController {

    @Autowired
    ArtistService artistService;


    @Autowired
    AlbumService albumService;

    @RequestMapping(method = RequestMethod.GET)
    public String home(final ModelMap model) {
        return "accueil";
    }
    //  ==================  ARTIST  ==================
    @RequestMapping(method = RequestMethod.GET, value="artists/{id}")
    public String getArtistById(@PathVariable("id") Long id, final ModelMap model) {
        Optional<Artist> artist = artistService.findArtistById(id);
        if (!artist.isPresent()) {
            model.put("artist", "Artist non trouvé");
        }
        model.put("artist", artist.get());
        return "detailArtist";
    }

    @RequestMapping(value = "artists", method = RequestMethod.GET)
    public String showAllPage(@RequestParam("page") Integer page_num,
                              @RequestParam("size") Integer size,
                              @RequestParam("sortProperty") String sortProperty,
                              @RequestParam("sortDirection") String sortDirection,
                              final ModelMap model) {
        Pageable pageable = PageRequest.of(page_num.intValue(), size.intValue(),
                            Sort.by(sortDirection.equalsIgnoreCase("ASC") ?
                            Order.asc(sortProperty) : Order.desc(sortProperty)));
        Page<Artist> page = artistService.findAll(pageable);

        model.put("artists", page);
        model.put("size", size);
        model.put("pageNum", page_num);
        model.put("sortDirection", sortDirection);
        model.put("sortProperty", sortProperty);
        model.put("pageTot", page.getTotalPages());
        model.put("sizep", String.format("Affichage des artistes %d à %d sur un total de %d",
        size * page_num /* Index elem 0 de la page */,
        size * (page_num + 1) /* Index dernier element de la page */,
        page.getTotalPages() - 1));
        return "listeArtists";
    }

    /**
     * <h3>Cherche un artiste avec le nom  contenant la chaine 'name' dans la database</h3>
     * @param name
     * @return
     */
    @RequestMapping(value = "artists", method = RequestMethod.POST, params = "name")
    public String showAllPage(@RequestParam(value = "name") String name, final ModelMap model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Order.asc("name")));
        Page<Artist> page = artistService.findByNameIgnoreCase(name, pageable);

        model.put("artists", page);
        model.put("size", 10);
        model.put("pageNum", 0);
        model.put("sortDirection", "ASC");
        model.put("sortProperty", "name");
        model.put("pageTot", page.getTotalPages());
        model.put("sizep", String.format("Affichage des artistes %d à %d sur un total de %d",
        0 /* Index elem 0 de la page */,
        10 /* Index dernier element de la page */,
        page.getTotalPages() - 1));
        return "listeArtists";
    }

    @RequestMapping(value="artists/delete/{id}", method = RequestMethod.GET)
    public RedirectView deleteArtist(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            albumService.delete(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Failure", "La supression n'a pas réussi !");
            // redirection sur l'artiste d'ou vien l'album
            new RedirectView("/thymeleaf/artists?page=0&size=10&sortProperty=name&sortDirection=ASC)");

        }
        redirectAttributes.addFlashAttribute("Success", "La supression a réussi !");
        // redirection sur l'artiste d'ou vien l'album
        return new RedirectView("/thymeleaf/artists?page=0&size=10&sortProperty=name&sortDirection=ASC)");
    }

    @RequestMapping(value = "artists/save/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createSaveAlbum(Artist artist, final ModelMap model) {
        artist = artistService.create(artist);
        model.put("artist", artist);
        return new RedirectView("artists/albums/" + artist.getId().toString());
    }

    //  ==================  FIN ARTIST  ==================

    //  ==================  ALBUMS  ==================
    @RequestMapping(value="artists/albums/delete/{id}", method = RequestMethod.GET)
    public RedirectView deleteAlbum(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Album> album = albumService.findById(id);

        if (album.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "L'album n'existe pas !");
            // redirection sur l'artiste d'ou vien l'album
            return new RedirectView("/thymeleaf/artists/" + album.get().getArtist().getId().toString());
        }
        try {
            albumService.delete(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "La supression n'a pas réussi !");
            // redirection sur l'artiste d'ou vien l'album
            new RedirectView("/thymeleaf/artists/" + album.get().getArtist().getId().toString());

        }
        redirectAttributes.addFlashAttribute("message", "La supression a réussi !");
        // redirection sur l'artiste d'ou vien l'album
        return new RedirectView("/thymeleaf/artists/" + album.get().getArtist().getId().toString());
    }

    @RequestMapping(value = "artists/albums/save/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createSaveAlbum(Album album, final ModelMap model) {
        album = albumService.create(album);
        model.put("album", album);
        return new RedirectView("artists/albums/" + album.getId().toString());
    }
    //  ==================  FIN ALBUMS  ==================
}
