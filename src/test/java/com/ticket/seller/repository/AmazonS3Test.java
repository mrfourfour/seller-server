package com.ticket.seller.repository;

import com.ticket.seller.config.AmazonS3Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AmazonS3Test {
    @Autowired
    private AmazonS3Config client;
    private String BUCKET = "ticket-mrfourfour-bucket";

    @Test
    public void create() {
        String KEY = "dockerfile4";
                client.s3AsyncClient();
                CompletableFuture<PutObjectResponse> future = client.s3AsyncClient()
                        .putObject(
                        PutObjectRequest.builder()
                                .bucket(BUCKET)
                                .key(KEY)
                                .build(),
                        AsyncRequestBody.fromFile(Paths.get("Dockerfile"))
                );
                future.whenComplete((resp, err) -> {
                    try {
                        if (resp != null) {
                            System.out.println("my response: " + resp);
                        } else {
                            err.printStackTrace();

                        }
                    } finally {
                        client.s3AsyncClient().close();
                    }
                });

                future.join();
            }

    @Test
    public void read() {
        String KEY = "Dockerfile";
        client.s3AsyncClient();
        final CompletableFuture<GetObjectResponse> futureGet = client.s3AsyncClient()
                .getObject(
                        GetObjectRequest.builder()

                                .bucket(BUCKET)
                                .key(KEY)
                                .build(),
                        AsyncResponseTransformer.toFile(Paths.get(KEY)));
        futureGet.whenComplete((resp, err) -> {
            try {
                if (resp != null) {
                    System.out.println(resp);
                } else {
                    // Handle error
                    err.printStackTrace();
                }
            } finally {
                // Lets the application shut down. Only close the client when you are completely done with it
                client.s3AsyncClient().close();
            }
        });
        futureGet.join();
    }
}

