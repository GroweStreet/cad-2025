package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.config.TestConfig;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringJUnitConfig(classes = TestConfig.class)
@Transactional
@DisplayName("Интеграционные тесты OrderService")
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category(1, "Корма", "Сухие и влажные корма");
        categoryRepository.save(category);

        product = new Product();
        product.setProductId(1);
        product.setName("Сухой корм для собак");
        product.setCategory(category);
        product.setPrice(new BigDecimal("1500.00"));
        product.setStockQuantity(50);
        productRepository.save(product);

        customer = new Customer(1, "Алексей Иванов", "alex@example.com", "+79261112233", "Москва");
        customerRepository.save(customer);
    }

    @Test
    @DisplayName("createOrder — заказ сохраняется в БД с корректными полями")
    void createOrder_persistsToDatabase() {
        Order order = orderService.createOrder(customer, product, 2);

        assertThat(order.getOrderId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo("NEW");
        assertThat(order.getTotalPrice()).isEqualByComparingTo("3000.00");
        assertThat(order.getCustomer().getCustomerId()).isEqualTo(1);
        assertThat(order.getShippingAddress()).isEqualTo("Москва");
    }

    @Test
    @DisplayName("findAll — возвращает созданные заказы")
    void findAll_returnsCreatedOrders() {
        orderService.createOrder(customer, product, 1);
        orderService.createOrder(customer, product, 3);

        List<Order> orders = orderService.findAll();

        assertThat(orders).hasSize(2);
    }

    @Test
    @DisplayName("createOrder — заказ содержит ровно одну позицию OrderDetail")
    void createOrder_hasOneOrderDetail() {
        Order order = orderService.createOrder(customer, product, 5);

        assertThat(order.getOrderDetails()).hasSize(1);
        assertThat(order.getOrderDetails().get(0).getQuantity()).isEqualTo(5);
        assertThat(order.getOrderDetails().get(0).getPrice())
                .isEqualByComparingTo("7500.00");
    }

    @Test
    @DisplayName("createOrder — нулевое количество не сохраняет заказ")
    void createOrder_zeroQuantity_doesNotPersistOrder() {
        assertThatThrownBy(() -> orderService.createOrder(customer, product, 0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(orderService.findAll()).isEmpty();
    }

    @Test
    @DisplayName("update — несуществующий ID заказа бросает NoSuchElementException")
    void update_nonExistentOrder_throwsException() {
        assertThatThrownBy(() -> orderService.update(999, "PAID", "somewhere"))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }
}
