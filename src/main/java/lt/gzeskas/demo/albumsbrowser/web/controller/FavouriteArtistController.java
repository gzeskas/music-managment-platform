package lt.gzeskas.demo.albumsbrowser.web.controller;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.FavouriteArtistService;
import lt.gzeskas.demo.albumsbrowser.web.view.ExternalArtist;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user/{userId}/settings/favourite/artist")
public class FavouriteArtistController {
    private final FavouriteArtistService favouriteArtistService;

    public FavouriteArtistController(FavouriteArtistService favouriteArtistService) {
        this.favouriteArtistService = favouriteArtistService;
    }

    @PutMapping
    public Mono<ExternalArtist> save(@PathVariable("userId") long userId,
                                     @RequestBody ExternalArtist externalArtist) {
        return favouriteArtistService.saveFavouriteArtist(userId, new Artist(externalArtist.getId(), externalArtist.getName()))
                .map(ExternalArtist::map);
    }

    @GetMapping
    public Mono<ExternalArtist> retrieve(@PathVariable("userId") long userId) {
        return favouriteArtistService.findFavouriteArtist(userId).map(ExternalArtist::map);
    }
}
