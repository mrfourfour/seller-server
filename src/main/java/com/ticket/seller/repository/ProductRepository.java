package com.ticket.seller.repository;

import com.ticket.seller.model.Product;
import com.ticket.seller.objectMapper.DynamoDbMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public Flux<Product> findSeller(String sellerId) {
        CompletableFuture<QueryResponse> res = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("PK-seller_id-index")
                .keyConditionExpression("PK = :pk and seller_id =:sellerId")
                .expressionAttributeValues(Map.of(
                        ":pk", AttributeValue.builder().s("Product").build(),
                        ":sellerId", AttributeValue.builder().s(sellerId).build()))
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
    public Mono<Product> findByProductId(String id) {
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
                        Map.entry("SK", AttributeValue.builder().s("9c138c6b-004c-4f4f-b808-eaa4c271c418").build()),
                        Map.entry("date", AttributeValue.builder().s(LocalDateTime.now().toString()).build()),
                        Map.entry("name", AttributeValue.builder().s("전시회테러").build()),
                        Map.entry("seller_id", AttributeValue.builder().s("Tester").build()),
                        Map.entry("image" , AttributeValue.builder().s("url").build()),
                        Map.entry("info" , AttributeValue.builder().s("테스트임당").build()),
                        Map.entry("options" , AttributeValue.builder().s("더미").build()),
                        Map.entry("area" , AttributeValue.builder().s("SEOUL").build()),
                        Map.entry("price" , AttributeValue.builder().n(String.valueOf(1111)).build()),
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
    public Mono<Product> save(Product product) {
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
    public Mono<Void> delete(String id) {
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName("ticket")
                .key(Map.of(
                        "PK", AttributeValue.builder().s("Product").build(),
                        "SK", AttributeValue.builder().s(id).build()
                ))
                .build();

        try{
            client.deleteItem(deleteItemRequest);
        }catch (DynamoDbException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done!");
        return null;
    }

}
