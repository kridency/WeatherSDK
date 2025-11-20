package org.example.weathersdk.repository;

import org.example.weathersdk.dto.City;
import org.example.weathersdk.dto.ForecastData;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

public class ForecastDataRepository implements CacheRepository<String, ForecastData> {
    private final Map<Map.Entry<Instant, Supplier<String>>, ForecastData> cache = Collections.synchronizedMap(
            new TreeMap<>(Map.Entry.<Instant, Supplier<String>>comparingByKey(Instant::compareTo).reversed())
    );

    @Override
    public ForecastData put(ForecastData value) {
        if (cache.size() == 10) clear(cache.entrySet().stream().reduce((a, b) -> b).get().getKey().getValue().get());
        cache.put(new AbstractMap.SimpleEntry<>(Instant.now(), value.getCity()::getName), value);
        return value;
    }

    @Override
    public Optional<ForecastData> get(String key) {
        return cache.entrySet().stream().filter(entry ->
                entry.getKey().getValue().get().equals(key)).map(Map.Entry::getValue).findFirst();
    }

    @Override
    public Optional<ForecastData> clear(String key) {
        return cache.keySet().stream().filter(x -> x.getValue().get().equals(key)).findFirst().map(cache::remove);
    }

    @Override
    public Collection<String> clearAll() {
        return cache.keySet().stream()
                .map(Map.Entry::getValue)
                .map(Supplier::get)
                .map(this::clear)
                .map(Optional::orElseThrow)
                .map(ForecastData::getCity)
                .map(City::getName)
                .toList();
    }
}
