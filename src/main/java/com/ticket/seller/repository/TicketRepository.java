package com.ticket.seller.repository;

import com.ticket.seller.model.Ticket;
import com.ticket.seller.objectMapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TicketRepository implements DynamoDbRepository<Ticket, String> {
    private final TicketMapper ticketMapper;
    private final DynamoDbAsyncClient client;

    @Override
    public Flux<Ticket> findAll() {
        CompletableFuture<QueryResponse> future = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("sortByDate")
                .keyConditionExpression("PK = :pk")
                .expressionAttributeValues(Map.of(":pk", AttributeValue.builder().s("Ticket").build()))
                .scanIndexForward(true)
                .build());

        CompletableFuture<List<Ticket>> ticketListFuture = future
                .thenApplyAsync(QueryResponse::items)
                .thenApplyAsync(list -> list.parallelStream()
                        .map(ticketMapper::toObj)
                        .collect(Collectors.toList())
                );
        return Mono.fromFuture(ticketListFuture).flatMapMany(Flux::fromIterable);
    }

    public Mono<Ticket> findById(String ticketId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("ticket")
                .key(Map.ofEntries(
                        Map.entry("PK", AttributeValue.builder().s("Ticket").build()),
                        Map.entry("SK", AttributeValue.builder().s(ticketId).build())
                ))
                .build();
        return Mono.fromFuture(client.getItem(getItemRequest)
            .thenApplyAsync(GetItemResponse::item)
            .thenApplyAsync(ticketMapper::toObj));
    }

    @Override
    public Mono<Ticket> findByProductId(String id) {
        return null;
    }


    @Override
    public Mono<Ticket> save(Ticket ticket) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName("ticket")
                .item(ticketMapper.toMap(ticket))
                .build();
        return Mono.fromFuture(client
                .putItem(request)
                .thenApplyAsync(PutItemResponse::attributes)
                .thenApplyAsync(ticketMapper::toObj));
    }

    public Flux<Ticket> findUserId(String userId) {
        CompletableFuture<QueryResponse> res = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("PK-seller_id-index")
                .keyConditionExpression("PK = :pk and seller_id = :seller")
                .filterExpression("user_id = :user")
                .expressionAttributeValues(Map.of(
                        ":pk", AttributeValue.builder().s("Ticket").build(),
                        ":seller", AttributeValue.builder().s("Naver").build(),
                        ":user", AttributeValue.builder().s(userId).build()))
                .build());

        CompletableFuture<List<Ticket>> listCompletableFuture = res
                .thenApplyAsync(QueryResponse::items)
                .thenApplyAsync(list -> list.parallelStream()
                        .map(ticketMapper::toObj)
                        .collect(Collectors.toList())
                );

        return Mono.fromFuture(listCompletableFuture).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Ticket> findSeller(String seller) {
        return null;
    }

    @Override
    public Mono<Void> delete(String id) {
        return null;
    }

    @Override
    public Mono<Ticket> saveTest() {
        return null;
    }


}
