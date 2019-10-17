package com.ticket.seller.repository;

import com.ticket.seller.model.Product;
import com.ticket.seller.model.Ticket;
import com.ticket.seller.objectMapper.DynamoDbMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@Repository
@RequiredArgsConstructor
public class ProductRepository implements DynamoDbRepository<Product, String>{
   private final DynamoDbMapper<Product> productMapper;
   private final DynamoDbAsyncClient client;

    @Override
    public Flux<Product> findAll() {
        CompletableFuture<QueryResponse> future = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("sortByDate")
                .keyConditionExpression("PK = :pk")
                .expressionAttributeValues(Map.of(":pk", AttributeValue.builder().s("Product").build()))
                .scanIndexForward(false)
                .build());

        CompletableFuture<List<Product>> productListFuture = future
                .thenApplyAsync(QueryResponse::items)
                .thenApplyAsync(list -> list.parallelStream()
                        .map(productMapper::toObj)
                        .collect(Collectors.toList())
                );

        return Mono.fromFuture(productListFuture).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Product> findSeller(String seller) {
        CompletableFuture<QueryResponse> res = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("PK-seller_id-index")
                .keyConditionExpression("PK = :pk and seller_id =:seller")
                .expressionAttributeValues(Map.of(
                        ":pk", AttributeValue.builder().s("Product").build(),
                        ":seller", AttributeValue.builder().s(seller).build()))
                .build());

        CompletableFuture<List<Product>> listCompletableFuture = res
                .thenApplyAsync(QueryResponse::items)
                .thenApplyAsync(list -> list.parallelStream()
                        .map(productMapper::toObj)
                        .collect(Collectors.toList())
                );

        return Mono.fromFuture(listCompletableFuture).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Product> findById(String id) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("ticket")
                .key(Map.of(
                        "PK", AttributeValue.builder().s("Product").build(),
                        "SK", AttributeValue.builder().s(id).build()))
                .build();
        return Mono.fromFuture(client
                .getItem(getItemRequest)
                .thenApplyAsync(GetItemResponse::item)
                .thenApplyAsync(productMapper::toObj));
    }

    @Override
    public Mono<Product> saveTest() {
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("ticket")
                .item(
                     Map.ofEntries(
                        Map.entry("PK", AttributeValue.builder().s("Product").build()),
                        Map.entry("SK", AttributeValue.builder().s(UUID.randomUUID().toString()).build()),
                        Map.entry("date", AttributeValue.builder().s("다이나모 시바꺼--;;").build()),
                        Map.entry("name", AttributeValue.builder().s("전시회테러").build()),
                        Map.entry("seller_id", AttributeValue.builder().s("Tester").build()),
                        Map.entry("image" , AttributeValue.builder().s("url").build()),
                        Map.entry("info" , AttributeValue.builder().s("테스트임당").build()),
                        Map.entry("option" , AttributeValue.builder().s("더미").build()),
                        Map.entry("area" , AttributeValue.builder().s("SEOUL").build()),
                        Map.entry("price" , AttributeValue.builder().n(String.valueOf(23333)).build()),
                        Map.entry("category" , AttributeValue.builder().s("TOUR").build()),
                        Map.entry("sub_category" , AttributeValue.builder().s("ATTRACTION").build())
                    ))
                .build();

        return Mono.fromFuture(client
                .putItem(putItemRequest)
                .thenApplyAsync(PutItemResponse::attributes)
                .thenApplyAsync(productMapper::toObj));
    }


    @Override
    public Mono<Product> save(Mono<Product> product) {
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("ticket")
                .item(productMapper.toMap(product))
                .build();
       return Mono.fromFuture(client
               .putItem(putItemRequest)
               .thenApplyAsync(PutItemResponse::attributes)
               .thenApplyAsync(productMapper::toObj));
    }
    @Override
    public Flux<Ticket> findUserId(String userId) {

        return null;
    }
    @Override
    public Mono<Void> delete() {
        return null;
    }
}
