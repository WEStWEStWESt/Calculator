package com.calculator.files;

import com.calculator.files.exceptions.FileNotFoundException;
import com.calculator.files.readers.FileReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LinearFileReader implements FileReader {
    private Path path;

    public LinearFileReader(String path) {
        this.path = Paths.get(path);
        if (Files.notExists(this.path)) {
            throw new FileNotFoundException(this.path);
        }
    }
}
