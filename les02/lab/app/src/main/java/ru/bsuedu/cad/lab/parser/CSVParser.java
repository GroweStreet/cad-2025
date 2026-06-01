package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVParser implements Parser {
    private static final String DELIMITER = ",";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();
        String[] lines = data.split("\\r?\\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(DELIMITER, -1);
            if (parts.length < 9) {
                System.err.println("Пропущена строка (недостаточно полей): " + line);
                continue;
            }

            try {
                long productId = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                String description = parts[2].trim();
                int categoryId = Integer.parseInt(parts[3].trim());
                BigDecimal price = new BigDecimal(parts[4].trim());
                int stockQuantity = Integer.parseInt(parts[5].trim());
                String imageUrl = parts[6].trim();
                Date createdAt = parseDate(parts[7].trim());
                Date updatedAt = parseDate(parts[8].trim());

                Product product = new Product(productId, name, description, categoryId,
                        price, stockQuantity, imageUrl, createdAt, updatedAt);
                products.add(product);
            } catch (Exception e) {
                System.err.println("Ошибка парсинга строки: " + line);
                e.printStackTrace();
            }
        }
        return products;
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Неверный формат даты: " + dateStr, e);
        }
    }
}