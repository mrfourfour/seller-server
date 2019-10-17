package com.ticket.seller.handler;

import com.ticket.seller.model.Product;
import com.ticket.seller.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHandler {
    private final ProductRepository productRepository;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<Product> pdInfo = productRepository.findAll();
        return ServerResponse.ok().body(pdInfo, Product.class);
    }

    public Mono<ServerResponse> saveTest(ServerRequest request) {
        productRepository.saveTest();
        return ServerResponse.ok().body(fromObject("데이터 SaveTest완료"));
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<Product> product = request.bodyToMono(Product.class);
        productRepository.save(product);
        return ServerResponse.ok().body(fromObject("데이터 Save완료"));
    }

    public Mono<ServerResponse> findSeller(ServerRequest request) {
        Flux<Product> sellerPd = productRepository.findSeller(request.pathVariable("seller"));
        return ServerResponse.ok().body(sellerPd, Product.class);
    }

//    public Mono<ServerResponse> save(ServerRequest request){
//        Mono<Product> pdInput = request.bodyToMono(Product.class);
//        return ServerResponse.ok().build(productRepository.save(pdInput));
//    }


//    public Mono<ServerResponse> getProduct(ServerRequest request) {
//        int pdId = Integer.parseInt(request.pathVariable("id"));
//        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
//        Mono<Product> pdInfo= productRepository.findById(pdId);
//        return pdInfo
//                .flatMap(product->
//                        ServerResponse.ok()
//                                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                                .body(fromObject(product))).defaultIfEmpty((ServerResponse) notFound);
//    }
}


