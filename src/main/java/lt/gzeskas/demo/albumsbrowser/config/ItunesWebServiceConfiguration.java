package lt.gzeskas.demo.albumsbrowser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.gzeskas.demo.albumsbrowser.services.cache.CachingAlbumSearchService;
import lt.gzeskas.demo.albumsbrowser.services.itunes.ItunesWebService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ItunesWebServiceConfiguration {

    @Bean
    WebClient itunesWebClient(@Value("${itunes-service.url}") String url) {
        return WebClient
                .builder()
                .baseUrl(url)
                .build();
    }

    @Bean
    ItunesWebService itunesWebService(WebClient webClient, ObjectMapper objectMapper) {
        return new ItunesWebService(webClient, objectMapper);
    }

    @Bean
    @Primary
    CachingAlbumSearchService cachingAlbumSearchService(ItunesWebService itunesWebService,
                                                        @Value("${itunes-service.requestLimitPerHour}") long requestLimitPerHour) {
        return new CachingAlbumSearchService(itunesWebService, requestLimitPerHour);
    }

}
