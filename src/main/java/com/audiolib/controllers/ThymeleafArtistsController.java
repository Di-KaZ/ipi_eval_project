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

    @GetMapping(value = "artists", params = { "page", "size", "sortProperty", "sortDirection" })
    public String showAllPage(@RequestParam("page") Integer pageNum, @RequestParam("size") Integer size,
            @RequestParam("sortProperty") String sortProperty, @RequestParam("sortDirection") String sortDirection,
            final ModelMap model) {
        Pageable pageable = PageRequest.of(pageNum.intValue(), size.intValue(),
                Sort.by(sortDirection.equalsIgnoreCase("ASC") ? Order.asc(sortProperty) : Order.desc(sortProperty)));
        Page<Artist> page = artistService.findAll(pageable);
        model.put("artists", page);
        model.put("size", size);
        model.put("pageNum", pageNum);
        model.put("sortDirection", sortDirection);
        model.put("sortProperty", sortProperty);
        model.put("pageTot", page.getTotalPages());
        model.put("sizep",
                String.format("Affichage des artistes %d à %d sur un total de %d",
                        size * pageNum /* Index elem 0 de la page */,
                        size * (pageNum + 1) /* Index dernier element de la page */, page.getTotalPages() - 1));
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

    /**
     * <h3>Cherche un artiste avec le nom contenant la chaine 'name' dans la
     * database</h3>
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "artists", params = { "name", "page", "size", "sortProperty",
            "sortDirection" }, method = RequestMethod.GET)
    public String showAllPageByName(@RequestParam(value = "name") String name, final ModelMap model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Order.asc("name")));
        Page<Artist> page = artistService.findByNameIgnoreCase(name, pageable);

        model.put("artists", page);
        model.put("size", 10);
        model.put("pageNum", 0);
        model.put("sortDirection", "ASC");
        model.put("sortProperty", "name");
        model.put("pageTot", page.getTotalPages());
        model.put("sizep", String.format("Affichage des artistes %d à %d sur un total de %d",
                0 /* Index elem 0 de la page */, 10 /* Index dernier element de la page */, page.getTotalPages() - 1));
        return "listeArtists";
    }

    @DeleteMapping(value = "artists/delete")
    public RedirectView deleteArtist(ArtistDTO artistDTO, RedirectAttributes redirectAttributes) {
        LOGGER.info("delete artist");
        try {
            artistService.delete(artistDTO.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "La supression n'a pas réussi !");
            // redirection sur l'artiste d'ou vien l'album
            new RedirectView("/thymeleaf/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");

        }
        redirectAttributes.addFlashAttribute("message", "La supression a réussi !");
        // redirection sur l'artiste d'ou vien l'album
        return new RedirectView("/thymeleaf/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }

    @PostMapping(value = "artists")
    public RedirectView createSaveArtist(ArtistDTO artistDTO, final ModelMap model) {
        Artist artist = artistService.create(artistDTO);
        if (artist == null) {
            model.put("message", String.format("Nom d'artiste %s invalide/Déjà existant", artistDTO.getName()));
            return new RedirectView("/thymeleaf/artists/" + artistDTO.getId().toString());
        }
        model.put("artist", artist);
        return new RedirectView("/thymeleaf/artists/" + artistDTO.getId().toString());
    }
}
