package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-тесты OrderService")
class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "Алексей Иванов", "alex@example.com", "+79261112233", "Москва, ул. Ленина, 10");

        product = new Product();
        product.setProductId(1);
        product.setName("Сухой корм для собак");
        product.setPrice(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("createOrder — успешное создание заказа")
    void createOrder_success() {
        Order saved = new Order();
        saved.setOrderId(1);
        saved.setStatus("NEW");
        saved.setTotalPrice(new BigDecimal("3000.00"));
        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        Order result = orderService.createOrder(customer, product, 2);

        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(1);
        assertThat(result.getStatus()).isEqualTo("NEW");
        assertThat(result.getTotalPrice()).isEqualByComparingTo("3000.00");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("createOrder — вызов save передаёт корректные данные заказа")
    void createOrder_verifyOrderFields() {
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(customer, product, 3);

        assertThat(result.getStatus()).isEqualTo("NEW");
        assertThat(result.getShippingAddress()).isEqualTo(customer.getAddress());
        assertThat(result.getTotalPrice()).isEqualByComparingTo(
                product.getPrice().multiply(BigDecimal.valueOf(3)));
        assertThat(result.getCustomer()).isEqualTo(customer);
    }

    @Test
    @DisplayName("createOrder — нулевое количество бросает IllegalArgumentException")
    void createOrder_zeroQuantity_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> orderService.createOrder(customer, product, 0))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("createOrder — null продукт бросает NullPointerException")
    void createOrder_nullProduct_throwsNullPointerException() {
        assertThatThrownBy(() -> orderService.createOrder(customer, null, 1))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("createOrder — null покупатель бросает NullPointerException")
    void createOrder_nullCustomer_throwsNullPointerException() {
        assertThatThrownBy(() -> orderService.createOrder(null, product, 1))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(orderRepository);
    }
}
