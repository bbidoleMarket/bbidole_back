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

    private static final String sql = """
    SELECT p.title, p.price, p.post_image_url, p.is_sold
    FROM chat_list c 
    JOIN posts p ON c.product_id = p.post_id 
    WHERE c.buyer_id = ? AND p.is_sold = 1 AND c.is_completed=1
    """;

    public List<PurchaseListResDto> findPurchaseListByBuyerId(Long buyerId){
        return jdbcTemplate.query(sql, new PostMapper(), buyerId);
    }

    private static class PostMapper implements RowMapper<PurchaseListResDto>{

        @Override
        public PurchaseListResDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PurchaseListResDto(
                rs.getString("title"),
                rs.getInt("price"),
                rs.getString("post_image_url"),
                rs.getBoolean("is_sold")
            );
        }
    }
}
