package lt.gzeskas.demo.albumsbrowser.web.controller;

import lt.gzeskas.demo.albumsbrowser.web.view.ExternalAlbum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@RequestMapping("/artist/{artistId}/albums")
public class AlbumsController {

    @GetMapping("/top5")
    public Flux<ExternalAlbum> getTop5Albums() {
        return Flux.empty();
    }

}
