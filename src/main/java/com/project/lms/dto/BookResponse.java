package com.project.lms.dto;

public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String description;
    private Double price;
    private String condition;
    private String status;
    private Long sellerId;

    public BookResponse() {
    }

    public BookResponse(
            Long id,
            String title,
            String author,
            String description,
            Double price,
            String condition,
            String status,
            Long sellerId) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.status = status;
        this.sellerId = sellerId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public String getStatus() {
        return status;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}