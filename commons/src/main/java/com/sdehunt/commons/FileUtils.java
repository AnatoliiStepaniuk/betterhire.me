package com.sdehunt.commons;

public class FileUtils {

    public static String fileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

}
