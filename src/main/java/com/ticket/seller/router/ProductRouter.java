package com.ticket.seller.router;

import com.ticket.seller.handler.HealthHandler;
import com.ticket.seller.handler.ProductHandler;
import com.ticket.seller.handler.TicketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

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
                        route(GET("/"),productHandler::findAll)
                            .andRoute(POST("/save"), productHandler::save)
                            .andRoute(GET("/{seller}") , productHandler::findSeller))
                .andNest(path("/api/ticket"),
                        route(GET("/"),ticketHandler::findAll)
                            .andRoute(GET("/{userId}"), ticketHandler::findUserByTicket));
//                          .andRoute(GET("/save"), ticketHandler::save));
    }
}
