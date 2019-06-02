package com.sdehunt.commons.cache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.time.Duration;

public class CacheImpl<K, V> implements Cache<K, V> {

    private org.ehcache.Cache<K, V> cache;

    public CacheImpl(String alias, Class<K> keyClass, Class<V> valueClass, long size, long ttlSecs) {

        CacheManager cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder().build();
        cacheManager.init();
        this.cache = cacheManager
                .createCache(alias, CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(
                                keyClass, valueClass,
                                ResourcePoolsBuilder.heap(size))
                        .withExpiry(ExpiryPolicyBuilder.expiry().create(Duration.ofSeconds(ttlSecs)).build()));
    }

    @Override
    public V get(K k) {
        return cache.get(k);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }
}
