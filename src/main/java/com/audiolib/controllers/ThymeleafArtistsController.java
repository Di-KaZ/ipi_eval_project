package com.audiolib.controllers;

import java.util.Optional;

import com.audiolib.persistance.model.Artist;
import com.audiolib.persistance.model.ArtistDTO;
import com.audiolib.persistance.service.ArtistService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/thymeleaf", method = RequestMethod.GET)
public class ThymeleafArtistsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThymeleafArtistsController.class);

    @Autowired
    ArtistService artistService;

    @GetMapping
    public String home(final ModelMap model) {
        return "accueil";
    }

    @GetMapping(value = "artists")
    public String showAllPage(@RequestParam(value = "name", required = false) String name,
            @RequestParam("page") Integer pageNum, @RequestParam("size") Integer size,
            @RequestParam("sortProperty") String sortProperty, @RequestParam("sortDirection") String sortDirection,
            final ModelMap model) {
        Pageable pageable = PageRequest.of(pageNum.intValue(), size.intValue(),
                Sort.by(sortDirection.equalsIgnoreCase("ASC") ? Order.asc(sortProperty) : Order.desc(sortProperty)));
        Page<Artist> page = artistService.findAll(pageable, name);
        model.put("artists", page);
        model.put("size", size);
        model.put("pageNum", pageNum);
        model.put("sortDirection", sortDirection);
        model.put("sortProperty", sortProperty);
        model.put("pageTot", page.getTotalPages() + 1);
        model.put("sizep",
                String.format("Affichage des artistes %d à %d sur un total de %d",
                        pageNum == 0 ? 1 : size * pageNum/* Index elem 0 de la page */,
                        size * (pageNum + 1) /* Index dernier element de la page */, page.getTotalPages()));
        return "listeArtists";
    }

    @GetMapping(value = "artists/{id}")
    public String getArtistById(@PathVariable("id") Long id, final ModelMap model) {
        Optional<Artist> artist = artistService.findById(id);
        if (artist.isEmpty()) {
            model.put("artist", "Artist non trouvé");
        } else {
            model.put("artist", artist.get());
        }
        return "detailArtist";
    }

    @DeleteMapping(value = "artists/delete")
    public RedirectView deleteArtist(ArtistDTO artistDTO, RedirectAttributes redirectAttributes) {
        LOGGER.info("delete artist");
        try {
            artistService.delete(artistDTO.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    String.format("La supression de \"%s\" n'a pas réussi !", artistDTO.getName()));
            // redirection sur l'artiste d'ou vien l'album
            new RedirectView("/thymeleaf/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
        }
        redirectAttributes.addFlashAttribute("message", "La supression a réussi !");
        // redirection sur l'artiste d'ou vien l'album
        return new RedirectView("/thymeleaf/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }

    @PostMapping(value = "add_artists")
    public RedirectView createSaveArtist(ArtistDTO artistDTO, RedirectAttributes redirectAttributes) {
        Artist artist = artistService.create(artistDTO);
        if (artist == null) {
            redirectAttributes.addFlashAttribute("message",
                    String.format("Nom d'artiste %s invalide/Déjà existant", artistDTO.getName()));
            return new RedirectView("/thymeleaf/artists/" + artistDTO.getId().toString());
        }
        redirectAttributes.addFlashAttribute("message", "Votre nouvel artiste a été crée !");
        return new RedirectView("/thymeleaf/artists/" + artist.getId().toString());
    }
}
