package com.project.lms.dto;

public class CreateOrderResponse {

    private String orderId;
    private String key;
    private Long amount;
    private String currency;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(
            String orderId,
            String key,
            Long amount,
            String currency) {

        this.orderId = orderId;
        this.key = key;
        this.amount = amount;
        this.currency = currency;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getKey() {
        return key;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}