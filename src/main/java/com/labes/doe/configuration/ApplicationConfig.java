package com.labes.doe.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class ApplicationConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${service.database.url}")
    private String url;

    @Value("${service.database.host}")
    private String host;

    @Value("${service.database.port}")
    private Integer port;

    @Value("${service.database.username}")
    private String username;

    @Value("${service.database.password}")
    private String password;

    @Value("${service.database.database}")
    private String database;

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {

        if(profile.equals("prod")) {
            url = url.split("postgres")[1];
            return ConnectionFactories.get("r2dbc:postgresql".concat(url));
        }

        Map<String, String> options = new HashMap<>();
        options.put("lock_timeout", "10s");

        return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .database(database)
                .options(options)
                .build());
    }
}
