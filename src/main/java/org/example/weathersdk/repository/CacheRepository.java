package org.example.weathersdk.repository;

import java.util.Collection;
import java.util.Optional;

public interface CacheRepository<K, V> {
    V put(V value);
    Optional<V> get(K objKey);
    Optional<V> clear(K objKey);
    Collection<K> clearAll();
}
