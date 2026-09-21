package com.project.lms.dto;

public class CreateOrderRequest {

    private Long bookId;

    public CreateOrderRequest() {
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}