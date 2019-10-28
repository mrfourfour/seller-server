package com.ticket.seller.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DynamoDbRepository<T, I> {

    Flux<T> findAll();
    Mono<T> findById(final I id);
    Mono<T> save(final Mono<T> t);

    Mono<T> saveTest();
    Flux<T> findSeller(final I seller);

    Mono<Void> delete();
}