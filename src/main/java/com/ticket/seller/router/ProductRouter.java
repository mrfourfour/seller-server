package com.ticket.seller.router;

import com.ticket.seller.handler.HealthHandler;
import com.ticket.seller.handler.ProductHandler;
import com.ticket.seller.handler.TicketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ProductRouter {
    private final ProductHandler productHandler;
    private final HealthHandler healthHandler;
    private final TicketHandler ticketHandler;
    @Bean
    public RouterFunction<ServerResponse> productRoute() {
        return RouterFunctions
                .nest(path("/"),
                        route(GET("/"), healthHandler::checkHealth))
                .andNest(path("/api/product"),
                        route(GET("/")
                            .and(accept(MediaType.APPLICATION_JSON_UTF8)),productHandler::findAll)
                        .andRoute(GET("/saveTest")
                            .and(accept(MediaType.APPLICATION_JSON_UTF8)), productHandler::saveTest)
                        .andRoute(GET("/save")
                            .and(accept(MediaType.APPLICATION_JSON_UTF8)), productHandler::save)
                        .andRoute(GET("/{seller}")
                            .and(accept(MediaType.APPLICATION_JSON_UTF8)), productHandler::findSeller)
                )
                .andNest(path("/api/ticket"),
                        route(GET("/")
                            .and(accept(MediaType.APPLICATION_JSON_UTF8)),ticketHandler::findAll)
                        .andRoute(POST("/save")
                            .and(contentType(MediaType.APPLICATION_JSON_UTF8)), ticketHandler::save)
                );

    }
}
