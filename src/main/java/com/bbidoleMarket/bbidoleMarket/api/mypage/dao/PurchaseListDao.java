package com.bbidoleMarket.bbidoleMarket.api.mypage.dao;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PurchaseListResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PurchaseListDao {
    private final JdbcTemplate jdbcTemplate;

    //전체 데이터 개수 쿼리 -> 전체 페이지 수 계산을 위해(개수만)
    private static final String COUNT_PURCHASE_LIST = """
            select count(*)
            from chat_list c
            join posts p on c.product_id = p.post_id
            where c.buyer_id = ? AND p.is_sold = 1 AND c.is_completed = 1
            """;

    //데이터 조회 쿼리 -> 현재 페이지에 보여줄 데이터 목록
    private static final String FIND_PURCHASE_LIST = """
            select p.title, p.price, p.post_image_url, p.is_sold, c.created_at, p.post_id
            FROM chat_list c
            JOIN posts p ON c.product_id = p.post_id
            WHERE c.buyer_id = ? AND p.is_sold = 1 AND c.is_completed=1
            ORDER BY c.created_at DESC
            LIMIT ? OFFSET ?
            """;

    //queryForObject(String sql, Class<T>) :단일 값 집계
    public Integer countPurchaseByBuyerId(Long buyerId) {
        //Integer.class타입으로 데이터 반환
        return jdbcTemplate.queryForObject(COUNT_PURCHASE_LIST, Integer.class, buyerId);
    }

    //query(String sql, Object[] args, RowMapper<T>) :파라미터가 있는 조건 select 조회
    public List<PurchaseListResDto> findPurchaseByBuyerId(Long buyerId, int limit, int offset) {
        return jdbcTemplate.query(FIND_PURCHASE_LIST, new Object[]{buyerId, limit, offset}, new PostMapper());
    }

//    private static final String sql = """
//    SELECT p.title, p.price, p.post_image_url, p.is_sold, c.created_at
//    FROM chat_list c
//    JOIN posts p ON c.product_id = p.post_id
//    WHERE c.buyer_id = ? AND p.is_sold = 1 AND c.is_completed=1
//    ORDER BY c.created_at DESC
//    """;
//
//    public List<PurchaseListResDto> findPurchaseListByBuyerId(Long buyerId){
//        return jdbcTemplate.query(sql, new PostMapper(), buyerId);
//    }

    private static class PostMapper implements RowMapper<PurchaseListResDto> {

        @Override
        public PurchaseListResDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PurchaseListResDto(
                    rs.getLong("post_id"),
                    rs.getString("title"),
                    rs.getInt("price"),
                    rs.getString("post_image_url"),
                    rs.getBoolean("is_sold"),
                    rs.getTimestamp("created_at").toLocalDateTime() //post생성시간
            );
        }
    }
}
