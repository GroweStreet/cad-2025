# Отчёт о лабораторной работе 6. Spring MVC + Thymeleaf

## Цель работы

Перейти с низкоуровневых сервлетов на Spring MVC: настроить `DispatcherServlet` через `WebApplicationInitializer`, реализовать REST API и веб-интерфейс заказов с Thymeleaf.

## Выполнение работы

### 1. Настройка Spring MVC

Вместо `web.xml` используется `AppInitializer implements WebApplicationInitializer`:

```java
public void onStartup(ServletContext container) {
    var context = new AnnotationConfigWebApplicationContext();
    context.register(AppConfig.class);
    var dispatcher = container.addServlet("dispatcher", new DispatcherServlet(context));
    dispatcher.setLoadOnStartup(1);
    dispatcher.addMapping("/");
}
```

В `AppConfig` добавлены аннотация `@EnableWebMvc` и бины Thymeleaf (`ClassLoaderTemplateResolver`, `SpringTemplateEngine`, `ThymeleafViewResolver`). Поддержка `LocalDateTime` в Jackson — через `JavaTimeModule`.

### 2. REST API заказов (`/api/orders`)

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/orders` | Список всех заказов |
| GET | `/api/orders/{id}` | Заказ по ID |
| POST | `/api/orders` | Создать: `{"customerId":1,"productId":1,"quantity":2}` |
| PUT | `/api/orders/{id}` | Обновить: `{"status":"PAID","shippingAddress":"..."}` |
| DELETE | `/api/orders/{id}` | Удалить |

Также сохранён `GET /api/products` из предыдущей работы.

### 3. Веб-интерфейс Thymeleaf (`/orders`)

| URL | Шаблон | Описание |
|---|---|---|
| `GET /orders` | `orders.html` | Таблица заказов, кнопки «Изменить» / «Удалить» |
| `GET /orders/new` | `order-form.html` | Форма создания |
| `POST /orders` | — | Создание, редирект на `/orders` |
| `GET /orders/{id}/edit` | `order-edit.html` | Форма редактирования статуса и адреса |
| `POST /orders/{id}/edit` | — | Обновление, редирект |
| `POST /orders/{id}/delete` | — | Удаление, редирект |

### 4. Сборка и деплой

```bash
gradle war   # → build/libs/product-table.war
```

Скопировать WAR в `$TOMCAT_HOME/webapps/`, запустить `bin/startup.bat`.

- Веб-интерфейс: `http://localhost:8080/product-table/orders`
- REST API: `http://localhost:8080/product-table/api/orders`

## UML-диаграмма классов

```mermaid
classDiagram
    class AppInitializer {
        +onStartup(ServletContext)
    }
    class AppConfig {
        +DataSource dataSource()
        +entityManagerFactory()
        +transactionManager()
        +templateResolver()
        +templateEngine()
        +viewResolver()
    }
    class OrderRestController {
        +getAll()
        +getById(id)
        +create(body)
        +update(id, body)
        +delete(id)
    }
    class ProductRestController {
        +getAll()
    }
    class OrderController {
        +list(model)
        +newForm(model)
        +create(params)
        +editForm(id, model)
        +update(id, params)
        +delete(id)
    }
    class OrderService {
        +createOrder(customer, product, qty)
        +findAll()
        +findById(id)
        +update(id, status, address)
        +delete(id)
    }
    class DataLoaderService {
        +load()
    }

    class Category { +Integer categoryId; +String name }
    class Product { +Integer productId; +String name; +BigDecimal price }
    class Customer { +Integer customerId; +String name; +String address }
    class Order { +Integer orderId; +String status; +BigDecimal totalPrice }
    class OrderDetail { +Integer quantity; +BigDecimal price }

    Category "1" --> "many" Product
    Customer "1" --> "many" Order
    Order "1" --> "many" OrderDetail
    Product "1" --> "many" OrderDetail

    AppInitializer --> AppConfig
    OrderRestController --> OrderService
    ProductRestController --> ProductRepository
    OrderController --> OrderService
    OrderService --> OrderRepository
    DataLoaderService --> CategoryRepository
    DataLoaderService --> ProductRepository
    DataLoaderService --> CustomerRepository
```

## Выводы

`DispatcherServlet` как единая точка входа упрощает маршрутизацию — не нужно регистрировать каждый сервлет вручную. `@RestController` возвращает JSON напрямую без `ObjectMapper.writeValue()`. Thymeleaf-шаблоны с Bootstrap обеспечивают читаемый HTML с минимальным кодом. `@Transactional` в сервисном слое гарантирует консистентность операций с БД.
