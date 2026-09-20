package com.project.lms.dto;

public class CreatePaymentOrderResponse {

    private String orderId;
    private String key;
    private Long amount;
    private String currency;
    private Long bookId;

    public CreatePaymentOrderResponse() {
    }

    public CreatePaymentOrderResponse(
            String orderId,
            String key,
            Long amount,
            String currency,
            Long bookId) {

        this.orderId = orderId;
        this.key = key;
        this.amount = amount;
        this.currency = currency;
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}