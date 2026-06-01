package ru.bsuedu.cad.lab.reader;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader {

    private final String fileName;

    public ResourceFileReader(@Value("${product.file}") String fileName) {
        this.fileName = fileName;
    }

    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader инициализирован: " + LocalDateTime.now());
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
