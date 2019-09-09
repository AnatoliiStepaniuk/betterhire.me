package com.sdehunt.commons.util;

import java.util.Set;
import java.util.stream.Collectors;

public class EnumUtils {

    public static String stringify(Set<? extends Enum> enums) {
        if (enums == null) {
            return null;
        }
        return enums.stream()
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));
    }
}
