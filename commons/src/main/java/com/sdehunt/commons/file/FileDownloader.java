package com.sdehunt.commons.file;

import java.nio.file.Path;

public interface FileDownloader {

    void download(String uri, Path targetFile);
}
