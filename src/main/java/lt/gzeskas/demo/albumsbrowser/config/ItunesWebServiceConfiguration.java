package lt.gzeskas.demo.albumsbrowser.config;

import lt.gzeskas.demo.albumsbrowser.services.itunes.ItunesWebService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItunesWebServiceConfiguration {

    @Bean
    ItunesWebService itunesWebService() {
        return new ItunesWebService();
    }

}
