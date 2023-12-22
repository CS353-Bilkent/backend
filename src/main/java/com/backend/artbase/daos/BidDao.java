package com.backend.artbase.daos;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.Bid;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BidDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public Optional<Bid> findById(Integer bidId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("bid_id", bidId);

        //@formatter:off
        String sql = "SELECT bid_id, auction_id, user_id, bid_amount, bid_status, bid_time " +
                     "FROM bid WHERE bid_id = :bid_id";
        //@formatter:on

        try {
            Bid bid = jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                //@formatter:off
                return Bid.builder()
                        .bidId(rsw.getLong("bid_id"))
                        .auctionId(rsw.getInteger("auction_id"))
                        .userId(rsw.getInteger("user_id"))
                        .bidAmount(rsw.getBigDecimal("bid_amount"))
                        .bidStatus(rsw.getBoolean("bid_status"))
                        .bidTime(rsw.getLocalDateTime("bid_time"))
                        .build();
                //@formatter:on
            });
            return Optional.ofNullable(bid);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Bid save(Bid bid) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("bid_id", bid.getBidId());
        params.put("bid_status", bid.getBidStatus());

        //@formatter:off
        String sql = "UPDATE bid SET bid_status = :bid_status WHERE bid_id = :bid_id";
        //@formatter:on

        jdbcTemplate.update(sql, params);
        return bid;
    }

    public List<Bid> findBidsByArtworkId(Integer artworkId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        String sql = "SELECT b.bid_id, b.auction_id, b.user_id, b.bid_amount, b.bid_status, b.bid_time " + "FROM bid b "
                + "JOIN auction a ON b.auction_id = a.auction_id " + "WHERE a.artwork_id = :artwork_id";

        try {
            return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return Bid.builder().bidId(rsw.getLong("bid_id")).auctionId(rsw.getInteger("auction_id")).userId(rsw.getInteger("user_id"))
                        .bidAmount(rsw.getBigDecimal("bid_amount")).bidTime(rsw.getLocalDateTime("bid_time")).build();
            });
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Integer getAuctionOwner(Integer bid_id) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("bid_id", bid_id);

        String sql = "SELECT a.user_id FROM bid b , auction a WHERE b.auction_id = a.auction_id AND b.bid_id = :bid_id";

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("user_id");
        });

    }

    public Long getNextBidId() {
        String sql = "SELECT nextval('bid_bid_id_seq') AS next_bid_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getLong("next_bid_id");
        });
    }

    public void makeBid(Bid bid) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("bid_id", bid.getBidId());
        params.put("auction_id", bid.getAuctionId());
        params.put("user_id", bid.getUserId());
        params.put("bid_amount", bid.getBidAmount());
        params.put("bid_time", bid.getBidTime());

        //@formatter:off
        String sql = "INSERT INTO bid(bid_id, auction_id, user_id, bid_amount, bid_time)"
                     + " VALUES (:bid_id, :auction_id, :user_id, :bid_amount, :bid_time)";
        //@formatter:on

        jdbcTemplate.update(sql, params);

    }
}
