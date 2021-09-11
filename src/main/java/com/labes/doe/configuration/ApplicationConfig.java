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
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableR2dbcRepositories
public class ApplicationConfig extends AbstractR2dbcConfiguration {

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        Map<String, String> options = new HashMap<>();
        options.put("lock_timeout", "10s");

        PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host("localhost")
                .port(5432)  // optional, defaults to 5432
                .username("postgres")
                .password("senha123")
                .database("doe")// optional
                .options(options) // optional
                .build());

       // connectionFactory.create();
        return connectionFactory;
    }
}
