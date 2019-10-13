package com.ticket.seller.objectMapper;

import com.ticket.seller.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Component
public class ProductMapper implements DynamoDbMapper<Product>{
    public Product toObj(Map<String, AttributeValue> map) {
        return Product.builder()
                .id(map.get("SK").s())
                .name(map.get("name").s())
                .sellerId(map.get("seller_id").s())
                .image(map.get("image").s())
                .category(Enum.valueOf(Product.ProductCategory.class, map.get("category").s()))
                .subCategory(Enum.valueOf(Product.ProductSubCategory.class, map.get("sub_category").s()))
                .info(map.get("info").s())
                .area(Product.ProductArea.valueOf(map.get("area").s()))
                .price(Long.parseLong(map.get("price").n()))
                .option(map.get("option").s())
                .date(map.get("date").s())
                .build();
    }

    public Map<String, AttributeValue> toMap(Mono<Product> product) {
        return Map.ofEntries(
                Map.entry("PK", AttributeValue.builder().s("Product").build()),
                Map.entry("SK", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getId))).build()),
                Map.entry("name", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getName))).build()),
                Map.entry("seller_id", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getSellerId))).build()),
                Map.entry("image", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getImage))).build()),
                Map.entry("category", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getCategory))).build()),
                Map.entry("sub_category", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getSubCategory))).build()),
                Map.entry("info", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getInfo))).build()),
                Map.entry("area", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getArea))).build()),
                Map.entry("price", AttributeValue.builder().n(String.valueOf(product.subscribe(Product::getPrice))).build()),
                Map.entry("option", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getOption))).build()),
                Map.entry("date", AttributeValue.builder().s(String.valueOf(product.subscribe(Product::getDate))).build())
        );
    }
}