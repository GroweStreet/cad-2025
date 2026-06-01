package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.query.CategoryRequest;
import ru.bsuedu.cad.lab.renderer.Renderer;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Renderer renderer = context.getBean(Renderer.class);
        renderer.render();

        CategoryRequest categoryRequest = context.getBean(CategoryRequest.class);
        categoryRequest.execute();
    }
}
