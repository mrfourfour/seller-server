package com.ticket.seller.objectMapper;

import com.ticket.seller.model.Product;
import com.ticket.seller.model.Ticket;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public interface DynamoDbMapper<T> {
    T toObj(Map<String, AttributeValue> map);
    Map<String, AttributeValue> toMap(Mono<T> t);
}