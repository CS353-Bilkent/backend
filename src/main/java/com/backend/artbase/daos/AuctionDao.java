package com.backend.artbase.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.Auction;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuctionDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public Integer getNextAuctionId() {
        String sql = "SELECT nextval('auction_auction_id_seq') AS next_auction_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("next_auction_id");
        });
    }

    public void createNewAuction(Auction auction) {

        CustomSqlParameters params = CustomSqlParameters.create();

        params.put("user_id", auction.getUserId());
        params.put("auction_id", auction.getAuctionId());
        params.put("auction_start_date", auction.getAuctionStartDate());
        params.put("auction_end_date", auction.getAuctionEndDate());
        params.put("artwork_id", auction.getArtworkId());
        params.put("initial_bid", auction.getInitialBid());
        params.put("active", true);

        //@formatter:off
        String sql =
         "INSERT INTO public.auction (user_id, auction_id, auction_start_date, auction_end_date, artwork_id, initial_bid, active)"
         +" VALUES (:user_id, :auction_id, :auction_start_date, :auction_end_date, :artwork_id, :initial_bid, :active)";
        //@formatter:on

        jdbcTemplate.update(sql, params);

    }

    public List<Auction> getAllAuctions() {

        CustomSqlParameters params = CustomSqlParameters.create();

        //@formatter:off
        String auctionSql =
                "SELECT user_id, auction_id, auction_start_date, auction_end_date, artwork_id, initial_bid, active " +
                "FROM public.auction " +
                "WHERE active = true";
        //@formatter:on

        return jdbcTemplate.query(auctionSql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            //@formatter:off
            return Auction.builder()
                    .userId(rsw.getInteger("user_id"))
                    .auctionId(rsw.getInteger("auction_id"))
                    .auctionStartDate(rsw.getLocalDateTime("auction_start_date"))
                    .auctionEndDate(rsw.getLocalDateTime("auction_end_date"))
                    .artworkId(rsw.getInteger("artwork_id"))
                    .initialBid(rsw.getBigDecimal("initial_bid"))
                    .build();
            //@formatter:on
        });
    }

    public Optional<Auction> getAuctionForArtwork(Integer artworkId) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        //@formatter:off
        String auctionForArtworkSql =
                "SELECT user_id, auction_id, auction_start_date, auction_end_date, artwork_id, initial_bid, active " +
                "FROM public.auction " +
                "WHERE artwork_id = :artwork_id AND active = true";
        //@formatter:on

        try {

            return Optional.of(jdbcTemplate.queryForObject(auctionForArtworkSql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                //@formatter:off
                return Auction.builder()
                .userId(rsw.getInteger("user_id"))
                .auctionId(rsw.getInteger("auction_id"))
                .auctionStartDate(rsw.getLocalDateTime("auction_start_date"))
                .auctionEndDate(rsw.getLocalDateTime("auction_end_date"))
                .artworkId(rsw.getInteger("artwork_id"))
                .initialBid(rsw.getBigDecimal("initial_bid"))
                .build();
                //@formatter:on
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Auction> getUserAuctions(Integer userId) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);
        //@formatter:off
        String userAuctionsSql =
                "SELECT user_id, auction_id, auction_start_date, auction_end_date, artwork_id, initial_bid " +
                "FROM public.auction " +
                "WHERE user_id = :user_id AND active = true";
        //@formatter:on

        return jdbcTemplate.query(userAuctionsSql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            //@formatter:off
            return Auction.builder()
                    .userId(rsw.getInteger("user_id"))
                    .auctionId(rsw.getInteger("auction_id"))
                    .auctionStartDate(rsw.getLocalDateTime("auction_start_date"))
                    .auctionEndDate(rsw.getLocalDateTime("auction_end_date"))
                    .artworkId(rsw.getInteger("artwork_id"))
                    .initialBid(rsw.getBigDecimal("initial_bid"))
                    .build();
            //@formatter:on
        });
    }

    public List<Auction> getInactiveAuctions() {

        CustomSqlParameters params = CustomSqlParameters.create();
        //@formatter:off
        String sql =
                "SELECT user_id, auction_id, auction_start_date, auction_end_date, artwork_id, initial_bid, active " +
                "FROM public.auction " +
                "WHERE active = false";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            //@formatter:off
            return Auction.builder()
                    .userId(rsw.getInteger("user_id"))
                    .auctionId(rsw.getInteger("auction_id"))
                    .auctionStartDate(rsw.getLocalDateTime("auction_start_date"))
                    .auctionEndDate(rsw.getLocalDateTime("auction_end_date"))
                    .artworkId(rsw.getInteger("artwork_id"))
                    .initialBid(rsw.getBigDecimal("initial_bid"))
                    .build();
            //@formatter:on
        });
    }
}
