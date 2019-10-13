package com.ticket.seller.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class HealthHandler {
    public Mono<ServerResponse> checkHealth(ServerRequest serverRequest) {

        return ServerResponse.ok().body(fromObject("seller Sever HttpStatus:" + "OK"));
    }
}
