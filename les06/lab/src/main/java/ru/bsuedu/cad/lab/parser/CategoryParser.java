package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Category;

import java.util.List;

public interface CategoryParser {
    List<Category> parse(String data);
}
