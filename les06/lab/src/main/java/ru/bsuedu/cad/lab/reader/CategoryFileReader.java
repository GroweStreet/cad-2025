package ru.bsuedu.cad.lab.reader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component("categoryFileReader")
public class CategoryFileReader implements Reader {

    private final String fileName;

    public CategoryFileReader(@Value("${category.file}") String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String read() {
        try {
            Path path = new ClassPathResource(fileName).getFile().toPath();
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл: " + fileName, e);
        }
    }
}
