package com.sdehunt.score.slides;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PicturesReaderTest {

    @Test
    public void verticalToPictureTest() {
        PicturesReader reader = new EagerPicturesReader();

        String pictureString = "V 2 selfie smile";
        Picture picture = reader.toPicture(pictureString);

        Assert.assertTrue(picture.isVertical());
        Assert.assertEquals(picture.getTags().size(), 2);
        Assert.assertTrue(picture.getTags().containsAll(Arrays.asList("selfie", "smile")));
    }

    @Test
    public void horizontalToPictureTest() {
        PicturesReader reader = new EagerPicturesReader();

        String pictureString = "H 3 cat beach sun";
        Picture picture = reader.toPicture(pictureString);

        Assert.assertFalse(picture.isVertical());
        Assert.assertEquals(picture.getTags().size(), 3);
        Assert.assertTrue(picture.getTags().containsAll(Arrays.asList("cat", "beach", "sun")));
    }

    @Test(expected = RuntimeException.class)
    public void invalidToPictureTest() {
        PicturesReader reader = new EagerPicturesReader();

        String pictureString = "invalid string";
        reader.toPicture(pictureString);
    }
}
