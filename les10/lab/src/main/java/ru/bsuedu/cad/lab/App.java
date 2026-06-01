package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.app.Client;
import ru.bsuedu.cad.lab.config.AppConfig;

public class App {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(Client.class).run();
        context.close();
    }
}
