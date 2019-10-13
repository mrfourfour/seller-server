package com.ticket.seller.objectMapper;

import com.ticket.seller.model.Product;
import com.ticket.seller.model.Ticket;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Component
public class TicketMapper implements DynamoDbMapper<Ticket> {
    public Ticket toObj(Map<String, AttributeValue> map) {
        return Ticket.builder()
                .id(map.get("SK").s())
                .productId(map.get("product_id").s())
                .userId(map.get("user_id").s())
                .amount(Integer.parseInt(map.get("amount").n()))
                .status(Enum.valueOf(Ticket.TicketStatus.class, map.get("status").s()))
                .date(map.get("date").s())
                .totalPrice(Long.parseLong(map.get("total_price").n()))
                .qrData(map.get("qr_data").s())
                .build();
    }

//    @Override
//    public Map<String, AttributeValue> toMap(Ticket ticket) {
//        return Map.of(
//                "PK", AttributeValue.builder().s("Ticket").build(),
//                "SK", AttributeValue.builder().s(ticket.getId()).build(),
//                "product_id", AttributeValue.builder().s(ticket.getProductId()).build(),
//                "user_id", AttributeValue.builder().s(ticket.getUserId()).build(),
//                "status", AttributeValue.builder().s(ticket.getStatus().getKey()).build(),
//                "amount", AttributeValue.builder().n(String.valueOf(ticket.getAmount())).build(),
//                "total_price", AttributeValue.builder().n(String.valueOf(ticket.getTotalPrice())).build(),
//                "date", AttributeValue.builder().s(ticket.getDate()).build(),
//                "qr_data", AttributeValue.builder().s(ticket.getQrData()).build()
//        );
//    }

    public Map<String, AttributeValue> toMap(Mono<Ticket> ticket) {
        return Map.of(
                "PK", AttributeValue.builder().s("Ticket").build(),
                "SK", AttributeValue.builder().s(String.valueOf(ticket.subscribe(Ticket::getId))).build(),
                "product_id", AttributeValue.builder().s(String.valueOf(ticket.subscribe(Ticket::getProductId))).build(),
                "user_id", AttributeValue.builder().s(String.valueOf(ticket.subscribe(Ticket::getUserId))).build(),
                "status", AttributeValue.builder().s(String.valueOf(ticket.subscribe(Ticket::getStatus))).build(),
                "amount", AttributeValue.builder().n(String.valueOf(ticket.subscribe(Ticket::getAmount))).build(),
                "total_price", AttributeValue.builder().n(String.valueOf(ticket.subscribe(Ticket::getTotalPrice))).build(),
                "date", AttributeValue.builder().s(String.valueOf(ticket.subscribe(Ticket::getDate))).build(),
                "qr_data", AttributeValue.builder().s(String.valueOf(ticket.subscribe(Ticket::getQrData))).build()
        );
    }
}