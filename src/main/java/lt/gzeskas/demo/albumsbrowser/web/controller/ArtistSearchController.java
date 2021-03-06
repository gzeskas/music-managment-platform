package lt.gzeskas.demo.albumsbrowser.web.controller;

import lt.gzeskas.demo.albumsbrowser.services.ArtistSearchService;
import lt.gzeskas.demo.albumsbrowser.web.view.ExternalArtist;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/search/artist")
public class ArtistSearchController {
    private final ArtistSearchService artistSearchService;

    public ArtistSearchController(ArtistSearchService artistSearchService) {
        this.artistSearchService = artistSearchService;
    }

    @GetMapping
    public Flux<ExternalArtist> search(@RequestParam("term") @NotBlank String term) {
        return artistSearchService.searchByTerm(term)
                .map(ExternalArtist::map);
    }

}
