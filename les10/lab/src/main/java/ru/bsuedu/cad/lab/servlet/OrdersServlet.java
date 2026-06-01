package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        OrderService orderService = ctx.getBean(OrderService.class);
        List<Order> orders = orderService.findAll();

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Заказы</title>");
        out.println("<style>body{font-family:Arial;padding:20px} table{border-collapse:collapse;width:100%} th,td{border:1px solid #ccc;padding:8px} th{background:#4CAF50;color:#fff}</style>");
        out.println("</head><body>");
        out.println("<h2>Список заказов</h2>");
        out.println("<a href='" + req.getContextPath() + "/orders/create'><button>Создать заказ</button></a><br><br>");
        out.println("<table><tr><th>ID</th><th>Покупатель</th><th>Дата</th><th>Сумма</th><th>Статус</th></tr>");
        for (Order o : orders) {
            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>%n",
                    o.getOrderId(),
                    o.getCustomer().getName(),
                    o.getOrderDate().toLocalDate(),
                    o.getTotalPrice(),
                    o.getStatus());
        }
        out.println("</table></body></html>");
    }
}
