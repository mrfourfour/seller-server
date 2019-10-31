package com.ticket.seller.handler;

import com.ticket.seller.model.Ticket;
import com.ticket.seller.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketHandler {
    private final TicketRepository ticketRepository;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<Ticket> ticketInfo = ticketRepository.findAll();
        return ServerResponse.ok().body(ticketInfo, Ticket.class);
    }

//    public Mono<ServerResponse> save(ServerRequest request){
//        Mono<Ticket> ticket = request.bodyToMono(Ticket.class);
//        ticketRepository.save(ticket);
//        return ServerResponse.ok().body(fromObject("데이터 Save완료"));
//    }

    public Mono<ServerResponse> findUserByTicket(ServerRequest request) {
        Flux<Ticket> userTicket = ticketRepository.findUserId(request.pathVariable("userId"));
        return ServerResponse.ok().body(userTicket, Ticket.class);
    }

}
