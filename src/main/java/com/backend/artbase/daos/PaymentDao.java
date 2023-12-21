package com.backend.artbase.daos;

import com.backend.artbase.entities.Payment;
import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PaymentDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public Optional<Payment> findById(Integer paymentId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("payment_id", paymentId);

        //@formatter:off
        String sql = "SELECT payment_id, buyer_id, seller_id, bid_id, workshop_id, approved, payment_type " +
                     "FROM payment WHERE payment_id = :payment_id";
        //@formatter:on

        try {
            Payment payment = jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                //@formatter:off
                return Payment.builder()
                        .paymentId(rsw.getInteger("payment_id"))
                        .buyerId(rsw.getInteger("buyer_id"))
                        .sellerId(rsw.getInteger("seller_id"))
                        .bidId(rsw.getLong("bid_id"))
                        .workshopId(rsw.getInteger("workshop_id"))
                        .approved(rsw.getBoolean("approved"))
                        .paymentType(rsw.getCharacter("payment_type"))
                        .build();
                //@formatter:on
            });
            return Optional.ofNullable(payment);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Payment save(Payment payment) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("payment_id", payment.getPaymentId());
        params.put("approved", payment.getApproved());

        //@formatter:off
        String sql = "UPDATE payment SET approved = :approved WHERE payment_id = :payment_id";
        //@formatter:on

        jdbcTemplate.update(sql, params);
        return payment;
    }

}