package com.calculator.files.exceptions;

import java.nio.file.Path;

public class FileUnreadableException extends FileException {
    public static final String UNREADABLE_FILE = "The file '%s' is unreadable.";

    public FileUnreadableException(Path path) {
        super(path == null ? NULL_PATH : UNREADABLE_FILE.formatted(path.toFile().getAbsolutePath()));
    }
}
