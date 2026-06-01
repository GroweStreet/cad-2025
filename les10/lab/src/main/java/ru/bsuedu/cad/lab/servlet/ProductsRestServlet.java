package ru.bsuedu.cad.lab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/products")
public class ProductsRestServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        List<Product> products = ctx.getBean(ProductRepository.class).findAll();

        List<Map<String, Object>> result = products.stream()
                .map(p -> Map.<String, Object>of(
                        "name", p.getName(),
                        "categoryName", p.getCategory().getName(),
                        "stockQuantity", p.getStockQuantity()
                ))
                .collect(Collectors.toList());

        resp.setContentType("application/json; charset=UTF-8");
        mapper.writeValue(resp.getWriter(), result);
    }
}
