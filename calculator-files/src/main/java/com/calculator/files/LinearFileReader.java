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
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/*
   1. У Ридера запрашивается строка.
   2. Ридер обращается к странице ранее полученных строк(Лист и т.д.).
     2.1 если страница есть, то берём первый элемент из страницы и удаляем его;
     2.2. если страницы нет, то:
         2.2.1 если есть LinesPager, то запрашиваем следующую страницу (hasNext, next).
             * если hasNext - true, тогда делаем полученную страницу текущей => переходим к п.2.
             * если hasNext - false, значит страницы нет => переходим к п.2.2.2.
         2.2.2 если нет LinesPager, то обращаемся к странице ранее полученных файлов:
              2.2.2.1 если сраница есть, то берём первый элемент из страницы,удаляем его и
                      создаём для полученного файла LinesPager => переходим к п.2.2.1.
              2.2.2.2 если страницы нет, то:
                 2.2.2.2a если есть FilesPager, то запрашиваем следующую страницу (hasNext, next).
                     * если hasNext - true, тогда делаем полученную страницу текущей => переходим к п.2.2.2.1
                     * если hasNext - false, значит страницы нет, LinearFileReader завершает работу.

*/
public class LinearFileReader implements FileReader {

    public static final int DEFAULT_LINES_PAGE_SIZE = 1000;
    public static final int DEFAULT_FILES_PAGE_SIZE = 100;
    public static final int DEFAULT_DEPTH = 4;
    private final Pager<Path> filesPager;
    private Pager<String> linesPager;
    private Queue<String> lines;
    private Queue<Path> files;
    private int linesPageSize;
    private int filesPageSize;
    private int depth;

    public LinearFileReader(String path) throws IOException, EmptyPageException {
        this(path, DEFAULT_DEPTH, DEFAULT_LINES_PAGE_SIZE, DEFAULT_FILES_PAGE_SIZE);
    }

    public LinearFileReader(String path, int depth, int linesPageSize, int filesPageSize) throws IOException, EmptyPageException {
        Path validPath = validatePath(path);
        this.filesPager = new FilesPager(validPath);
        setValidOrDefault(depth, DEFAULT_DEPTH, this::setDepth);
        setValidOrDefault(linesPageSize, DEFAULT_LINES_PAGE_SIZE, this::setLinesPageSize);
        setValidOrDefault(filesPageSize, DEFAULT_FILES_PAGE_SIZE, this::setFilesPageSize);
        filesPager.init();
    }

    @Override
    public boolean hasNext() {
        // 2
        if (lines != null && !lines.isEmpty()) {
            return true; // 2.1 + next()
        }
        // 2.2
        if (linesPager != null && !linesPager.isEmpty()) {
            lines = linesPager.next(); // 2.2.1
            return hasNext();
        }
        // 2.2.2
        if (files != null && !files.isEmpty()) {
            resolveLinesReader(files.poll()); // 2.2.2.1
            return hasNext();
        }
        // 2.2.2.2
        if (!filesPager.isEmpty()) {
            files = filesPager.next(); // 2.2.2.2a
            return hasNext();
        }
        return false;
    }

    @Override
    public String next() {
        return lines.poll();
    }

    private void resolveLinesReader(Path path) {
        linesPager = new LinesPager(path);
        try {
            linesPager.init();
        } catch (IOException e) {
            System.out.println("Unable to get a lines page of file [" + path + "]: " + e.getMessage());
        } catch (EmptyPageException e) {
            System.out.println("Unable to get a lines page of file [" + path + "]: file is empty.");
        }
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

    private void setValidOrDefault(int value, int defaultValue, Consumer<Integer> setter) {
        setter.accept(value <= 0 ? defaultValue : value);
    }

    private void setDepth(int depth) {
        this.depth = depth;
    }

    private void setLinesPageSize(int linesPageSize) {
        this.linesPageSize = linesPageSize;
    }

    private void setFilesPageSize(int filesPageSize) {
        this.filesPageSize = filesPageSize;
    }

    private class FilesPager extends Pager<Path> {

        FilesPager(Path path) {
            super(path, filesPageSize);
        }

        @Override
        Queue<Path> getPage() throws IOException {
            return Files.walk(path, depth)
                    .filter(Files::isRegularFile)
                    .skip(getOffset())
                    .limit(filesPageSize)
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    private class LinesPager extends Pager<String> {
        LinesPager(Path path) {
            super(path, linesPageSize);
        }

        @Override
        Queue<String> getPage() throws IOException {
            return Files.lines(path)
                    .skip(getOffset())
                    .limit(linesPageSize)
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    private abstract class Pager<T> implements Iterator<Queue<T>> {
        private final int limit;
        protected Path path;
        protected Queue<T> page;
        private int pageNumber;

        Pager(Path path, int limit) {
            this.path = path;
            this.limit = limit;
            pageNumber++;
        }

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
        public Queue<T> next() {
            Queue<T> page = this.page;
            pageNumber++;
            this.page = null;
            return page;
        }

        protected int getOffset() {
            return pageNumber == 1 ? 0 : limit * pageNumber;
        }

        void init() throws IOException, EmptyPageException {
            page = getPage();
            if (page == null || page.isEmpty()) {
                throw new EmptyPageException();
            }
        }

        boolean isEmpty() {
            return !hasNext();
        }

        abstract Queue<T> getPage() throws IOException;
    }
}
