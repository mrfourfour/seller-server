package com.ticket.seller.handler;

import com.ticket.seller.model.Product;
import com.ticket.seller.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return request.bodyToMono(Product.class)
                .doOnNext(product -> {
                    product.setId(UUID.randomUUID().toString());
                    product.setDate(LocalDateTime.now().toString());
                    product.setOptions(product.getOptions().parallelStream().map(option -> {
                        option.setId(UUID.randomUUID().toString());
                        return option;
                    }).collect(Collectors.toList()));
                    product.setReviews(new HashSet<Product.Review>());
                })
                .doOnNext(product -> {
                    log.info(product.toString());
                    productRepository.save(product);
                })
                .flatMap(product -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(product))
                        .switchIfEmpty(ServerResponse.badRequest()
                                .body(fromObject("등록 실패..")))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(Product.class)
                .doOnNext(product -> {
                    product.setDate(LocalDateTime.now().toString());
                    product.setReviews(new HashSet<Product.Review>());
                })
                .doOnNext(product -> {
                    log.info(product.toString());
                    productRepository.save(product);
                })
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(fromObject(product))
                        .switchIfEmpty(ServerResponse.badRequest()
                                .body(fromObject("수정 실패..")))
                );
    }

    public Mono<ServerResponse> findSeller(ServerRequest request) {
        Flux<Product> sellerPd = productRepository.findSeller(request.pathVariable("sellerId"));
        return ServerResponse.ok().body(sellerPd, Product.class);
    }

    public Mono<ServerResponse> findByProductId(ServerRequest request) {
        Mono<Product> findPdId = productRepository.findByProductId(request.pathVariable("productId"));
        return ServerResponse.ok().body(findPdId, Product.class);
    }


    public Mono<ServerResponse> delete(ServerRequest request) {
        productRepository.delete(request.pathVariable("productId"));
        return ServerResponse.ok().body(fromObject("상품이 삭제되었습니다."));

    }


}


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



