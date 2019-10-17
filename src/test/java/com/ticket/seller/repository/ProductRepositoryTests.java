package com.ticket.seller.repository;

import com.ticket.seller.config.dynamoDbConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest
public class ProductRepositoryTests {
    @Autowired
    private dynamoDbConfig client;

    @Test
    @Ignore
    public void listTable(){
        ListTablesRequest request = ListTablesRequest.builder().build();
        CompletableFuture<ListTablesResponse> response
                = client.dynamoDbAsyncClient().listTables(request);
        CompletableFuture<List<String>> tableNames = response.thenApply(ListTablesResponse::tableNames);
        tableNames.whenComplete((tables, err) -> {
            try {
                if (tables != null) {
                    tables.forEach(System.out::println);
                    System.out.println("Table List : " + tables);
                } else {
                    // Handle error
                    err.printStackTrace();
                }
            } finally {
                // Lets the application shut down. Only close the client when you are completely done with it.
                client.dynamoDbAsyncClient().close();
            }
        });
        tableNames.join();
    }
}
