package ru.bsuedu.cad.lab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderRestController(OrderService orderService,
                               CustomerRepository customerRepository,
                               ProductRepository productRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Order> getAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") Integer id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order create(@RequestBody Map<String, Integer> body) {
        var customer = customerRepository.findById(body.get("customerId")).orElseThrow();
        var product  = productRepository.findById(body.get("productId")).orElseThrow();
        return orderService.createOrder(customer, product, body.get("quantity"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable("id") Integer id,
                                        @RequestBody Map<String, String> body) {
        if (orderService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(
                orderService.update(id, body.get("status"), body.get("shippingAddress")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (orderService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
