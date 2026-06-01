package ru.bsuedu.cad.lab.renderer;

import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import java.util.List;
import java.util.Date;

public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider productProvider;

    public ConsoleTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    @Override
    public void render() {
        List<Product> products = productProvider.getProducts();

        if (products.isEmpty()) {
            System.out.println("Нет данных для отображения.");
            return;
        }

        String format = "| %-5s | %-20s | %-30s | %-10s | %-10s | %-5s | %-30s | %-12s | %-12s |%n";
        System.out.format("+-------+----------------------+--------------------------------+------------+------------+-------+--------------------------------+--------------+--------------+%n");
        System.out.format(format, "ID", "Name", "Description", "CategoryId", "Price", "Stock", "ImageUrl", "CreatedAt", "UpdatedAt");
        System.out.format("+-------+----------------------+--------------------------------+------------+------------+-------+--------------------------------+--------------+--------------+%n");

        for (Product p : products) {
            System.out.format(format,
                    p.getProductId(),
                    truncate(p.getName(), 20),
                    truncate(p.getDescription(), 30),
                    p.getCategoryId(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    truncate(p.getImageUrl(), 30),
                    truncateDate(p.getCreatedAt()),
                    truncateDate(p.getUpdatedAt())
            );
        }
        System.out.format("+-------+----------------------+--------------------------------+------------+------------+-------+--------------------------------+--------------+--------------+%n");
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 3) + "...";
    }

    private String truncateDate(Date date) {
        if (date == null) return "";
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
