package com.calculator.files;

import com.calculator.files.exceptions.EmptyPageException;
import com.calculator.files.exceptions.FileNotFoundException;
import com.calculator.files.exceptions.FileUnreadableException;
import com.calculator.files.readers.FileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/*
* Добавить исключения на проверки IsRegularFile и isReadable,
* желательно вынести все проверки в отдельный метод.
* почитать про способы чтения файлов с использованием java.nio.Files.
* Желательно организовать постраничное чтение
* почитать про методы lines и walk,
* ну и в целом ознакомиться с функционалом библиотеки.

    Чтение файла частями.
    (create Inner class) Использовать walk, чтобы добраться до нужного файла(директории).Скипаем ненужное.Читаем по
    страницам.(по 100 файлов)
    Использовать lines (читаем по станицам, 1000 строк) Для этого создать итератор, который будет проверять
    наличие "СЛЕДУЮЩЕЙ ЧАСТИ ДЛЯ ЧТЕНИЯ", и,собственно РИДЕР для чтения.
    В стриме использовать лямбду с телом{}...внутри  ставим точку останова для debug.
    Files.walk().map(path ->

    СОЗДАТЬ ИСКЛЮЧЕНИЕ ДЛЯ NULL-АРГУМЕНТА, ПРИШЕДШЕГО В КОНСТРУКТОР.
    */
    /*
        //  ---  walk
        try (Stream<Path> walk = Files.walk(filePath, 4)) {
            walk.forEach(System.out::println);
        } catch (Exception e) {
            throw new FileNotFoundException(filePath);
        }
        //  ---  lines
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(System.out::println);
        } catch (Exception e) {
            throw new FileNotFoundException(filePath);
        }*/
public class LinearFileReader implements FileReader {
    private Path path;

    public LinearFileReader(String path) {
        this.path = validatePath(path);
    }

    private Path validatePath(String path) {
        if (path == null || path.isBlank()) {
            throw new FileNotFoundException("File path is null or blank.");
        }
        Path filePath = Paths.get(path);
        if (Files.notExists(filePath)) {
            throw new FileNotFoundException(filePath);
        }
        if (!Files.isReadable(filePath)) {
            throw new FileUnreadableException(filePath);
        }
        return filePath;
    }

    private class Pager implements Iterator<List<Path>> {
        private Path path;
        private List<Path> page;

        Pager(Path path) {
            this.path = path;
        }

        void init() throws IOException {
            page = getPage();
            if (page == null || page.isEmpty()) {
                throw new EmptyPageException();
            }
        }

        List<Path> getPage() throws IOException {
            return Files.walk(LinearFileReader.this.path, 4)
                    .filter(Files::isDirectory)
                    .skip(100)
                    .limit(150)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean hasNext() {
            //TODO the page field should be checked on NULL or Empty. If empty - return False, otherwise - true.
            return false;
        }

        @Override
        public List<Path> next() {
            //TODO return the Page field.
            return null;
        }
    }
}
