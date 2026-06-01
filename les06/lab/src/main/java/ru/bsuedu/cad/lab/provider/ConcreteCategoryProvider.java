package ru.bsuedu.cad.lab.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.parser.CategoryParser;
import ru.bsuedu.cad.lab.reader.Reader;

import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {

    private final Reader reader;
    private final CategoryParser parser;

    @Autowired
    public ConcreteCategoryProvider(@Qualifier("categoryFileReader") Reader reader,
                                     CategoryParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Category> getCategories() {
        return parser.parse(reader.read());
    }
}
