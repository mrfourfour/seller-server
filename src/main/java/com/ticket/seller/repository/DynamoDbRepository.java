package com.ticket.seller.repository;

import com.ticket.seller.model.Ticket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DynamoDbRepository<T, I> {

    Flux<T> findAll();
    Mono<T> findById(final I id);
    Mono<T> save(final Mono<T> t);

    Mono<T> saveTest();
    Flux<T> findSeller(final I seller);

    Flux<Ticket> findUserId(final I userId);

    Mono<Void> delete();
}