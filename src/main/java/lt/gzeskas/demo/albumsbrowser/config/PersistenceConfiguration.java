package lt.gzeskas.demo.albumsbrowser.config;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    public Jdbi jdbi() {
       var jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        jdbi.useHandle(handle -> handle.execute(getInitialSqlCreateScript()));
       return jdbi;
    }

    public static String getInitialSqlCreateScript() {
        return "CREATE TABLE settings (id bigint primary key, favourite_artist_id bigint, favourite_artist_name text);";
    }

}
