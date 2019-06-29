package com.sdehunt.commons.file;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class UnirestFileDownloader implements FileDownloader {

    @Override
    @SneakyThrows({UnirestException.class, IOException.class})
    public void download(String uri, Path targetFile) {
        InputStream inputStream = Unirest.get(uri).asBinary().getBody();
        Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
