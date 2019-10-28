package com.ticket.seller.repository;

import com.ticket.seller.config.dynamoDbConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DynamoDbConfigTest {
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
