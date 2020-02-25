package lt.gzeskas.demo.albumsbrowser.web.controller;

import lt.gzeskas.demo.albumsbrowser.services.AlbumsSearchService;
import lt.gzeskas.demo.albumsbrowser.web.view.ExternalAlbum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/artist/{artistId}/albums")
public class AlbumsController {
    private final AlbumsSearchService albumsSearchService;

    public AlbumsController(AlbumsSearchService albumsSearchService) {
        this.albumsSearchService = albumsSearchService;
    }

    @GetMapping("/top5")
    public Flux<ExternalAlbum> getTop5Albums(@PathVariable("artistId") long artistId) {
        return albumsSearchService.findTop5ByArtist(artistId)
                .map(ExternalAlbum::map);
    }

}
