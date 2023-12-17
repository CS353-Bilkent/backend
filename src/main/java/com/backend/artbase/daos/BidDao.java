package com.backend.artbase.daos;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BidDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public List<Bid> findBidsByArtworkId(Integer artworkId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        String sql = 
            "SELECT b.bid_id, b.auction_id, b.user_id, b.bid_amount, b.bid_time " +
            "FROM bid b " +
            "JOIN auction a ON b.auction_id = a.auction_id " +
            "WHERE a.artwork_id = :artwork_id";

        try {
            return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return Bid.builder()
                    .bidId(rsw.getLong("bid_id"))
                    .auctionId(rsw.getInteger("auction_id"))
                    .userId(rsw.getInteger("user_id"))
                    .bidAmount(rsw.getBigDecimal("bid_amount"))
                    .bidTime(rsw.getLocalDateTime("bid_time"))
                    .build();
            });
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
