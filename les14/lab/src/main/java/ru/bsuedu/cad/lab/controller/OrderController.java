package ru.bsuedu.cad.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderService orderService,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "order-form";
    }

    @PostMapping
    public String create(@RequestParam("customerId") Integer customerId,
                         @RequestParam("productId") Integer productId,
                         @RequestParam("quantity") Integer quantity) {
        var customer = customerRepository.findById(customerId).orElseThrow();
        var product  = productRepository.findById(productId).orElseThrow();
        orderService.createOrder(customer, product, quantity);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        Order order = orderService.findById(id).orElseThrow();
        model.addAttribute("order", order);
        return "order-edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Integer id,
                         @RequestParam("status") String status,
                         @RequestParam("shippingAddress") String shippingAddress) {
        orderService.update(id, status, shippingAddress);
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        orderService.delete(id);
        return "redirect:/orders";
    }
}
