package com.calculator.files.exceptions;

import java.nio.file.Path;

public class FileNotFoundException extends FileException {
    public static final String NOT_FOUND_PATH = "File '%s' not found";

    public FileNotFoundException(Path path) {
        super(NOT_FOUND_PATH.formatted(path.toFile().getAbsolutePath()));
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
