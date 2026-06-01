# Отчет о лабораторной работе 1. Gradle. Базовое приложение Spring

## Цель работы

Создать каркас консольного приложения на основе фреймворка Spring с Java-конфигурацией. Реализовать загрузчик CSV-файлов и вывод данных о товарах зоомагазина в виде таблицы в консоль.

## Выполнение работы

### 1. Создание Gradle-проекта

Проект инициализирован командой `gradle init` со следующими параметрами:
- Package: `ru.bsuedu.cad.lab`
- Project name: `product-table`
- Type: Application
- Language: Java
- Java version: 17
- Structure: Single application project
- DSL: Kotlin
- Test framework: JUnit Jupiter

### 2. Добавление зависимости Spring

В файл `app/build.gradle.kts` добавлена зависимость:

```kotlin
implementation("org.springframework:spring-context:6.2.2")
```

### 3. Структура приложения

Реализованы следующие классы согласно диаграмме из задания:

| Класс / Интерфейс | Назначение |
|---|---|
| `Reader` | Интерфейс чтения данных |
| `ResourceFileReader` | Читает CSV-файл из classpath через `ClassPathResource` |
| `Parser` | Интерфейс парсинга данных |
| `CSVParser` | Разбирает CSV-строку в список объектов `Product` |
| `ProductProvider` | Интерфейс получения списка товаров |
| `ConcreteProductProvider` | Объединяет `Reader` и `Parser`, возвращает список товаров |
| `Renderer` | Интерфейс вывода данных |
| `ConsoleTableRenderer` | Выводит список товаров в консоль в виде таблицы |
| `Product` | Сущность «Товар» с полями: id, name, description, categoryId, price, stockQuantity, imageUrl, createdAt, updatedAt |

### 4. Spring Java-конфигурация

Все бины зарегистрированы в классе `AppConfig`, аннотированном `@Configuration`. Контекст создаётся через `AnnotationConfigApplicationContext`:

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
Renderer renderer = context.getBean(Renderer.class);
renderer.render();
```

### 5. CSV-файл

Файл `products.csv` размещён в `src/main/resources`. Содержит 10 товаров с полями: product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at.

### 6. Запуск приложения

Приложение запускается командой:

```bash
gradle run
```

Результат — таблица товаров, выведенная в консоль.

## Выводы

В ходе работы был изучен принцип инверсии управления (IoC) и внедрения зависимостей (DI) на практике. Spring-контейнер самостоятельно создаёт и связывает бины согласно Java-конфигурации, что избавляет от ручного управления зависимостями. Java-конфигурация (`@Configuration` + `@Bean`) обеспечивает типобезопасность и удобство рефакторинга по сравнению с XML-конфигурацией. Gradle обеспечивает автоматическую загрузку зависимостей и сборку проекта без ручного управления classpath.
