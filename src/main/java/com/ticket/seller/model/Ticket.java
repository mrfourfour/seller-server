package com.ticket.seller.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket implements Serializable {
    private String id;
    private TicketStatus status;
    private Integer amount;
    private Long totalPrice;
    private String productId;
    private String optionId;
    private String userId;
    private String date;
//    private String qrData;

    public enum TicketStatus {
        ON_PROGRESS("사용중"),
        NOT_USED("미사용"),
        USED("사용됨"),
        CANCELED("취소");

        private String value;

        TicketStatus(String value) {
            this.value = value;
        }

        public String getKey() {
            return name();
        }
        public String getValue() { return value; }
    }

    public String getQrData() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Map.ofEntries(
                Map.entry("id", this.id),
                Map.entry("status", this.status),
                Map.entry("amount", this.amount),
                Map.entry("totalPrice", this.totalPrice),
                Map.entry("productId", this.productId),
                Map.entry("optionId", this.optionId),
                Map.entry("userId", this.userId),
                Map.entry("date", this.date)
        ));
    }
}
