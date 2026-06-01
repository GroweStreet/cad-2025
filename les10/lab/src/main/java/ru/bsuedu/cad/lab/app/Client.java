package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.DataLoaderService;
import ru.bsuedu.cad.lab.service.OrderService;

@Component
public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final DataLoaderService dataLoaderService;
    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public Client(DataLoaderService dataLoaderService, OrderService orderService,
                  CustomerRepository customerRepository, ProductRepository productRepository) {
        this.dataLoaderService = dataLoaderService;
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public void run() {
        dataLoaderService.load();

        var customer = customerRepository.findById(1).orElseThrow();
        var product  = productRepository.findById(1).orElseThrow();

        Order order = orderService.createOrder(customer, product, 2);
        LOGGER.info("Создан заказ: id={}, покупатель='{}', товар='{}', кол-во=2, итого={}, статус={}",
                order.getOrderId(),
                order.getCustomer().getName(),
                order.getOrderDetails().get(0).getProduct().getName(),
                order.getTotalPrice(),
                order.getStatus());

        LOGGER.info("Все заказы в БД:");
        for (Order o : orderService.findAll()) {
            LOGGER.info("  id={}, покупатель='{}', итого={}, дата={}",
                    o.getOrderId(), o.getCustomer().getName(),
                    o.getTotalPrice(), o.getOrderDate());
        }
    }
}
