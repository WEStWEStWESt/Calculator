package com.calculator.files.exceptions;

import java.nio.file.Path;

public class FileNotFoundException extends RuntimeException {
    public static final String NULL_PATH = "File path is null.";
    public static final String NOT_FOUND_PATH = "File '%s' not found";

    public FileNotFoundException(Path path) {
        super(path == null ? NULL_PATH : NOT_FOUND_PATH.formatted(path.toFile().getAbsolutePath()));
    }
}
