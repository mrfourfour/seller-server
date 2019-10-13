package com.ticket.seller.handler;

import com.ticket.seller.model.Ticket;
import com.ticket.seller.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TicketHandler {
    private final TicketRepository ticketRepository;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<Ticket> ticketInfo = ticketRepository.findAll();
        return ServerResponse.ok().body(ticketInfo, Ticket.class);
    }

    public Mono<ServerResponse> save(ServerRequest request){

        return null;
    }
}
