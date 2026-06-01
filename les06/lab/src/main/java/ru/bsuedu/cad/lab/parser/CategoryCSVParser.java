package ru.bsuedu.cad.lab.parser;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryCSVParser implements CategoryParser {

    private static final String DELIMITER = ",";

    @Override
    public List<Category> parse(String data) {
        List<Category> categories = new ArrayList<>();
        String[] lines = data.split("\\r?\\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(DELIMITER, -1);
            if (parts.length < 3) {
                System.err.println("Пропущена строка (недостаточно полей): " + line);
                continue;
            }
            try {
                int categoryId = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String description = parts[2].trim();
                categories.add(new Category(categoryId, name, description));
            } catch (Exception e) {
                System.err.println("Ошибка парсинга строки: " + line);
            }
        }
        return categories;
    }
}
