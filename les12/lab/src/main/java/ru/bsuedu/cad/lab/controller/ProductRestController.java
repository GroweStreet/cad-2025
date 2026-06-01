package ru.bsuedu.cad.lab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return productRepository.findAll().stream()
                .map(p -> Map.<String, Object>of(
                        "name", p.getName(),
                        "categoryName", p.getCategory().getName(),
                        "stockQuantity", p.getStockQuantity()
                ))
                .collect(Collectors.toList());
    }
}
