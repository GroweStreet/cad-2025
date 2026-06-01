package ru.bsuedu.cad.lab.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CategoryRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute() {
        String sql = """
                SELECT c.category_id, c.name, COUNT(p.product_id) AS product_count
                FROM CATEGORIES c
                JOIN PRODUCTS p ON c.category_id = p.category_id
                GROUP BY c.category_id, c.name
                HAVING COUNT(p.product_id) > 1
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        LOGGER.info("Категории с более чем одним товаром:");
        for (Map<String, Object> row : rows) {
            LOGGER.info("  id={}, name='{}', кол-во товаров={}",
                    row.get("CATEGORY_ID"),
                    row.get("NAME"),
                    row.get("PRODUCT_COUNT"));
        }
    }
}
