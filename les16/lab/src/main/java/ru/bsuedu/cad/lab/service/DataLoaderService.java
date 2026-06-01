package ru.bsuedu.cad.lab.service;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

@Service
public class DataLoaderService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataLoaderService(CategoryRepository categoryRepository,
                             ProductRepository productRepository,
                             CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void load() {
        if (categoryRepository.count() > 0) return;
        loadCategories();
        loadProducts();
        loadCustomers();
    }

    private void loadCategories() {
        try {
            var lines = Files.readAllLines(
                    new ClassPathResource("category.csv").getFile().toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String[] p = lines.get(i).split(",", 3);
                categoryRepository.save(new Category(
                        Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки category.csv", e);
        }
    }

    private void loadProducts() {
        try {
            var lines = Files.readAllLines(
                    new ClassPathResource("product.csv").getFile().toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String[] p = lines.get(i).split(",", 9);
                Product product = new Product();
                product.setProductId(Integer.parseInt(p[0].trim()));
                product.setName(p[1].trim());
                product.setDescription(p[2].trim());
                product.setCategory(categoryRepository.getReferenceById(Integer.parseInt(p[3].trim())));
                product.setPrice(new BigDecimal(p[4].trim()));
                product.setStockQuantity(Integer.parseInt(p[5].trim()));
                product.setImageUrl(p[6].trim());
                product.setCreatedAt(LocalDate.parse(p[7].trim()));
                product.setUpdatedAt(LocalDate.parse(p[8].trim()));
                productRepository.save(product);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки product.csv", e);
        }
    }

    private void loadCustomers() {
        try {
            var lines = Files.readAllLines(
                    new ClassPathResource("customer.csv").getFile().toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String[] p = lines.get(i).split(",", 5);
                customerRepository.save(new Customer(
                        Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки customer.csv", e);
        }
    }
}
