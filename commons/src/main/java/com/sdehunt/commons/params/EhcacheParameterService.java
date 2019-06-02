package com.sdehunt.commons.params;

import com.sdehunt.commons.cache.Cache;
import com.sdehunt.commons.cache.CacheImpl;

import java.util.Optional;

public class EhcacheParameterService implements ParameterService {

    private Cache<String, String> cache;
    private ParameterService inner;

    public EhcacheParameterService(ParameterService inner) {
        this.cache = new CacheImpl<>("params", String.class, String.class, 20, 30);
        this.inner = inner;
    }

    @Override
    public String get(String param) {
        return Optional.ofNullable(cache.get(param)).orElseGet(() -> populateAndGet(param));
    }

    private String populateAndGet(String param) {
        String value = inner.get(param);
        cache.put(param, value);
        return value;
    }
}
