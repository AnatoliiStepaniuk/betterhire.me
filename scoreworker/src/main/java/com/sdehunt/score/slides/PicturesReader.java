package com.sdehunt.score.slides;

import java.util.Map;

public interface PicturesReader {

    Map<Integer, Picture> readPictures(String fileName);

}
