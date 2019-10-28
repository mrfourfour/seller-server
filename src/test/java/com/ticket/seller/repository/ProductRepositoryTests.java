package com.ticket.seller.repository;

import com.ticket.seller.objectMapper.ProductMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRepositoryTests {
    @Autowired
    private DynamoDbAsyncClient client;
    @Autowired
    private ProductMapper productMapper;
//    @Autowired
//    private AmazonS3Config amazonS3Config;

    @Test
//    @Ignore
    public void createProduct() {
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("ticket")
                .item(
                        Map.ofEntries(
                            Map.entry("PK", AttributeValue.builder().s("Test").build()),
                            Map.entry("SK", AttributeValue.builder().s(UUID.randomUUID().toString()).build()),
                            Map.entry("date", AttributeValue.builder().s("ZZZZZ").build()),
                            Map.entry("name", AttributeValue.builder().s("createTest").build()),
                            Map.entry("seller_id", AttributeValue.builder().s("Tester").build()),
                            Map.entry("image" , AttributeValue.builder().s("url").build()),
                            Map.entry("info" , AttributeValue.builder().s("테스트임당").build()),
                            Map.entry("option" , AttributeValue.builder().s("더미").build()),
                            Map.entry("area" , AttributeValue.builder().s("SEOUL").build()),
                            Map.entry("price" , AttributeValue.builder().n(String.valueOf(7777)).build()),
                            Map.entry("category" , AttributeValue.builder().s("TOUR").build()),
                            Map.entry("sub_category" , AttributeValue.builder().s("ATTRACTION").build())
                        ))
                .build();

             Mono.fromFuture(client
                .putItem(putItemRequest)
                .thenApplyAsync(PutItemResponse::attributes)
                .thenApplyAsync(productMapper::toObj));
//             client.getItem(GetItemRequest.builder().build())
    }


}
