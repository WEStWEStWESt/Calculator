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
 Добавить исключения на проверки IsRegularFile и isReadable,
  желательно вынести все проверки в отдельный метод.
  почитать про способы чтения файлов с использованием java.nio.Files.
  Желательно организовать постраничное чтение
  почитать про методы lines и walk,
  ну и в целом ознакомиться с функционалом библиотеки.

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

    private final Pager filesPager;

    public LinearFileReader(String path) throws IOException, EmptyPageException {
        Path validPath = validatePath(path);
        this.filesPager = new FilesPager(validPath);
        filesPager.init();
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

    private class FilesPager extends Pager<Path> {
        public static final int LIMIT = 100;
        public static final int DEPTH = 4;

        FilesPager(Path path) {
            super(path, LIMIT);
        }

        @Override
        List<Path> getPage() throws IOException {
            return Files.walk(path, DEPTH)
                    .filter(Files::isRegularFile)
                    .skip(getOffset())
                    .limit(LIMIT)
                    .collect(Collectors.toList());
        }
    }

    private class LinesPager extends Pager<String> {
        public static final int LIMIT = 1000;

        LinesPager(Path path) {
            super(path, LIMIT);
        }

        @Override
        List<String> getPage() throws IOException {
            return Files.lines(path)
                    .skip(getOffset())
                    .limit(LIMIT)
                    .collect(Collectors.toList());
        }
    }

    private abstract class Pager<T> implements Iterator<List<T>> {
        private int limit;
        protected Path path;
        protected List<T> page;
        private int pageNumber;

        Pager(Path path, int limit) {
            this.path = path;
            this.limit = limit;
            pageNumber++;
        }

        void init() throws IOException, EmptyPageException {
            page = getPage();
            if (page == null || page.isEmpty()) {
                throw new EmptyPageException();
            }
        }

        abstract List<T> getPage() throws IOException;

        @Override
        public boolean hasNext() {
            if (page == null || page.isEmpty()) {
                try {
                    getPage();
                } catch (IOException e) {
                    System.out.println("Unable to get page [" + pageNumber + "] : " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            return page != null && !page.isEmpty();
        }

        @Override
        public List<T> next() {
            List<T> page = this.page;
            pageNumber++;
            this.page = null;
            return page;
        }

        protected int getOffset() {
            return pageNumber == 1 ? 0 : limit * pageNumber;
        }
    }
}
