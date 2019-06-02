package com.sdehunt.commons.cache;

import java.util.Objects;
import java.util.function.Function;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value);

    default V computeIfAbsent(K key,
                              Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

}
