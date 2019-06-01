package com.sdehunt.commons.params;

import java.util.HashMap;
import java.util.Map;

// TODO create implementation with TTL
public class HardCachedParameterService implements ParameterService{
    private final Map<String, String> cache = new HashMap<>();
    private final ParameterService inner;

    public HardCachedParameterService(ParameterService inner) {
        this.inner = inner;
    }

    @Override
    public String get(String param) {
        return cache.computeIfAbsent(param, inner::get);
    }
}
