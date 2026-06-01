package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders/create")
public class CreateOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        List<Customer> customers = ctx.getBean(CustomerRepository.class).findAll();
        List<Product>  products  = ctx.getBean(ProductRepository.class).findAll();

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Создать заказ</title>");
        out.println("<style>body{font-family:Arial;padding:20px} label{display:block;margin:8px 0 2px} select,input{padding:6px;width:300px}</style>");
        out.println("</head><body><h2>Создать заказ</h2>");
        out.println("<form method='post' action='" + req.getContextPath() + "/orders/create'>");

        out.println("<label>Покупатель</label><select name='customerId'>");
        for (Customer c : customers)
            out.printf("<option value='%d'>%s</option>%n", c.getCustomerId(), c.getName());
        out.println("</select>");

        out.println("<label>Товар</label><select name='productId'>");
        for (Product p : products)
            out.printf("<option value='%d'>%s (%.2f руб.)</option>%n", p.getProductId(), p.getName(), p.getPrice());
        out.println("</select>");

        out.println("<label>Количество</label><input type='number' name='quantity' value='1' min='1'>");
        out.println("<br><br><button type='submit'>Оформить заказ</button>");
        out.println("</form></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        var ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        int customerId = Integer.parseInt(req.getParameter("customerId"));
        int productId  = Integer.parseInt(req.getParameter("productId"));
        int quantity   = Integer.parseInt(req.getParameter("quantity"));

        Customer customer = ctx.getBean(CustomerRepository.class).findById(customerId).orElseThrow();
        Product  product  = ctx.getBean(ProductRepository.class).findById(productId).orElseThrow();
        ctx.getBean(OrderService.class).createOrder(customer, product, quantity);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
