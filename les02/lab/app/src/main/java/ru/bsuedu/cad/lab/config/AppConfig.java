package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bsuedu.cad.lab.parser.CSVParser;
import ru.bsuedu.cad.lab.parser.Parser;
import ru.bsuedu.cad.lab.provider.ConcreteProductProvider;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import ru.bsuedu.cad.lab.reader.Reader;
import ru.bsuedu.cad.lab.reader.ResourceFileReader;
import ru.bsuedu.cad.lab.renderer.ConsoleTableRenderer;
import ru.bsuedu.cad.lab.renderer.Renderer;

@Configuration
public class AppConfig {

    @Bean
    public Reader reader() {
        return new ResourceFileReader("products.csv");
    }

    @Bean
    public Parser parser() {
        return new CSVParser();
    }

    @Bean
    public ProductProvider productProvider(Reader reader, Parser parser) {
        return new ConcreteProductProvider(reader, parser);
    }

    @Bean
    public Renderer renderer(ProductProvider productProvider) {
        return new ConsoleTableRenderer(productProvider);
    }
}