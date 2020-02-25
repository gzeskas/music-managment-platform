package lt.gzeskas.demo.albumsbrowser.config;

import lt.gzeskas.demo.albumsbrowser.repository.UserSettingsRepository;
import lt.gzeskas.demo.albumsbrowser.repository.local.JdbiUserSettingRepository;
import lt.gzeskas.demo.albumsbrowser.services.UserSettingsService;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoriesConfiguration {

    @Bean
    UserSettingsRepository userSettingsRepository(Jdbi jdbi){
        return new JdbiUserSettingRepository(jdbi);
    }

    @Bean
    UserSettingsService userSettingsService(UserSettingsRepository userSettingsRepository) {
        return new UserSettingsService(userSettingsRepository);
    }

}
