package com.ticket.seller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class AmazonS3Config {
    @Value("${aws.access.key}")
    private String AWS_ACCESS_KEY;
    @Value("${aws.secret.key}")
    private String AWS_SECRET_KEY;
    private String region = "ap-northeast-2";

    @Bean
    public S3AsyncClient s3AsyncClient() {
        return  S3AsyncClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(AWS_ACCESS_KEY,AWS_SECRET_KEY)
                    ))
                    .build();
     }
}
