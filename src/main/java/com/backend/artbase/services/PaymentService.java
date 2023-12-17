package com.backend.artbase.services;

import com.backend.artbase.entities.Payment;
import com.backend.artbase.daos.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    public Payment approvePayment(Integer paymentId) throws Exception {
        Payment payment = paymentDao.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment not found with id: " + paymentId));
        
        payment.setApproved(true);
        return paymentDao.save(payment);
    }

    public Payment rejectPayment(Integer paymentId) throws Exception {
        Payment payment = paymentDao.findById(paymentId)
            .orElseThrow(() -> new Exception("Payment not found with id: " + paymentId));

        payment.setApproved(false);
        return paymentDao.save(payment);
    }
}