package com.labes.doe.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

@Component
@RequiredArgsConstructor
public class AwsCredentialsConfig implements AwsCredentialsProvider {

    @Value("${amazon.s3.access-key}")
    private String accessKey;

    @Value("${amazon.s3.secret-key}")
    private String secretKey;

    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }
}
