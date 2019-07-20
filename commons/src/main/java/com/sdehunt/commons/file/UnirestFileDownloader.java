package com.sdehunt.commons.file;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnirestFileDownloader implements FileDownloader {

    @Override
    @SneakyThrows({UnirestException.class, IOException.class})
    public void download(String uri, String targetFile) {
        if (Files.exists(Path.of(targetFile))) {
            Files.delete(Path.of(targetFile));
        }
        Unirest.get(uri).asFile(targetFile);
    }
}
