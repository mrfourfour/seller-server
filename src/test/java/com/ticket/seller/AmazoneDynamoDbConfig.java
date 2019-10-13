package com.ticket.seller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.ticket.seller.config.dynamoDbConfig;
import com.ticket.seller.model.Product;
import com.ticket.seller.objectMapper.DynamoDbMapper;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.management.monitor.MonitorNotification;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest
public class AmazoneDynamoDbConfig {
    @Autowired
    private dynamoDbConfig dynamoDbConfig;

    @Test
    public void getDesc(){
        DescribeTableRequest request = DescribeTableRequest.builder().tableName("ticket").build();
        CompletableFuture<DescribeTableResponse> response
                = dynamoDbConfig.dynamoDbAsyncClient().describeTable(request);
        CompletableFuture<TableDescription> desc = response.thenApply(DescribeTableResponse::table);
//        desc.whenComplete((tables, err) -> {
//            try {
//                if (tables != null) {
//                    tables.attributeDefinitions();
//                } else {
//                    // Handle error
//                    err.printStackTrace();
//                }
//            } finally {
//                System.out.println(desc);
//                // Lets the application shut down. Only close the client when you are completely done with it.
//                dynamoDbConfig.dynamoDbAsyncClient().close();
//            }
//        });
//        desc.join();
    }


    @Test
    public void listTable(){
        ListTablesRequest request = ListTablesRequest.builder().build();
        CompletableFuture<ListTablesResponse> response
                = dynamoDbConfig.dynamoDbAsyncClient().listTables(request);
        CompletableFuture<List<String>> tableNames = response.thenApply(ListTablesResponse::tableNames);
        tableNames.whenComplete((tables, err) -> {
            try {
                if (tables != null) {
                    tables.forEach(System.out::println);
                } else {
                    // Handle error
                    err.printStackTrace();
                }
            } finally {
                // Lets the application shut down. Only close the client when you are completely done with it.
                dynamoDbConfig.dynamoDbAsyncClient().close();
            }
        });
        tableNames.join();
    }

}






