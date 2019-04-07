package com.sdehunt.score.slides;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Picture {
    private final boolean isVertical;
    private final Set<String> tags;

    public Picture(boolean isVertical, Collection<String> tags) {
        this.isVertical = isVertical;
        this.tags = new HashSet<>(tags);
    }

    public boolean isVertical() {
        return isVertical;
    }

    public Set<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return isVertical == picture.isVertical &&
                Objects.equals(tags, picture.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isVertical, tags);
    }
}
