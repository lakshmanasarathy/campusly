package com.project.lms.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayClient razorpayClient;

    public RazorpayService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    public Order createOrder(Double amount) throws Exception {

        long amountInPaise = Math.round(amount * 100);

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "book_" + System.currentTimeMillis());

        return razorpayClient.orders.create(orderRequest);
    }
}