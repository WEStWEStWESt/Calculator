package com.calculator.files.exceptions;

public class FileException extends RuntimeException{
    public static final String NULL_PATH = "File path is null.";

    public FileException(String message) {
        super(message);
    }
}
