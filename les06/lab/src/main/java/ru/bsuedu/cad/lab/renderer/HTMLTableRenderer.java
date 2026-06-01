package ru.bsuedu.cad.lab.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {

    private static final String OUTPUT_FILE = "products.html";

    private final ProductProvider productProvider;

    @Autowired
    public HTMLTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    @Override
    public void render() {
        List<Product> products = productProvider.getProducts();

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"ru\">\n<head>\n")
            .append("<meta charset=\"UTF-8\">\n")
            .append("<title>Товары зоомагазина</title>\n")
            .append("<style>\n")
            .append("body { font-family: Arial, sans-serif; padding: 20px; }\n")
            .append("table { border-collapse: collapse; width: 100%; }\n")
            .append("th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }\n")
            .append("th { background-color: #4CAF50; color: white; }\n")
            .append("tr:nth-child(even) { background-color: #f2f2f2; }\n")
            .append("</style>\n</head>\n<body>\n")
            .append("<h2>Товары зоомагазина</h2>\n")
            .append("<table>\n<thead>\n<tr>")
            .append("<th>ID</th><th>Название</th><th>Описание</th>")
            .append("<th>Категория</th><th>Цена</th><th>Остаток</th>")
            .append("<th>Изображение</th><th>Создан</th><th>Обновлён</th>")
            .append("</tr>\n</thead>\n<tbody>\n");

        for (Product p : products) {
            html.append("<tr>")
                .append("<td>").append(p.getProductId()).append("</td>")
                .append("<td>").append(escape(p.getName())).append("</td>")
                .append("<td>").append(escape(p.getDescription())).append("</td>")
                .append("<td>").append(p.getCategoryId()).append("</td>")
                .append("<td>").append(p.getPrice()).append("</td>")
                .append("<td>").append(p.getStockQuantity()).append("</td>")
                .append("<td><a href=\"").append(escape(p.getImageUrl())).append("\">ссылка</a></td>")
                .append("<td>").append(formatDate(p.getCreatedAt())).append("</td>")
                .append("<td>").append(formatDate(p.getUpdatedAt())).append("</td>")
                .append("</tr>\n");
        }

        html.append("</tbody>\n</table>\n</body>\n</html>");

        try (PrintWriter writer = new PrintWriter(OUTPUT_FILE, StandardCharsets.UTF_8)) {
            writer.print(html);
            System.out.println("HTML-таблица сохранена в файл: " + OUTPUT_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи HTML-файла: " + OUTPUT_FILE, e);
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
