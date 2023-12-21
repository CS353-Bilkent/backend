package com.backend.artbase.controllers;

import com.backend.artbase.entities.Payment;
import com.backend.artbase.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Endpoint to approve a payment
    @PutMapping("/approve/{paymentId}")
    public ResponseEntity<?> approvePayment(@PathVariable Integer paymentId) {
        try {
            Payment payment = paymentService.approvePayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reject/{paymentId}")
    public ResponseEntity<?> rejectPayment(@PathVariable Integer paymentId) {
        try {
            Payment payment = paymentService.rejectPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}