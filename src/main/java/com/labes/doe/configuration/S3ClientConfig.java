package com.labes.doe.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
@RequiredArgsConstructor
public class S3ClientConfig {

    private final AwsCredentialsConfig awsCredentialsConfig;

    @Bean
    public S3AsyncClient s3AsyncClient() {
        return S3AsyncClient
                .builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(awsCredentialsConfig)
                .build();
    }

}
