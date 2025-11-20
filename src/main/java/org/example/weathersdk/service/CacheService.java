package org.example.weathersdk.service;

import java.io.IOException;
import java.time.Instant;
import java.util.function.Function;

public interface CacheService<T> {

    T getRawDataObject(Function<T, Instant> function, String... description) throws IOException;
    T refreshCache(String... description) throws IOException;
    void refreshCache();
}
