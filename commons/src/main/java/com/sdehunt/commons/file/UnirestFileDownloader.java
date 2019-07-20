package com.sdehunt.commons.file;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.SneakyThrows;

public class UnirestFileDownloader implements FileDownloader {

    @Override
    @SneakyThrows({UnirestException.class})
    public void download(String uri, String targetFile) {
        Unirest.get(uri).asFile(targetFile);
    }
}
