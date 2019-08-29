package com.sdehunt.commons.util;

import java.util.Set;
import java.util.stream.Collectors;

public class EnumUtils {

    public static String stringify(Set<? extends Enum> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream()
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));
    }
}
