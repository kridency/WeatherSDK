package org.example.weathersdk.repository;

import org.example.weathersdk.dto.CurrentData;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

public class CurrentDataRepository implements CacheRepository<String, CurrentData> {
    private final Map<Map.Entry<Instant, Supplier<String>>, CurrentData> cache = Collections.synchronizedMap(
            new TreeMap<>(Map.Entry.<Instant, Supplier<String>>comparingByKey(Instant::compareTo).reversed())
    );

    @Override
    public CurrentData put(CurrentData value) {
        if (cache.size() == 10) clear(cache.entrySet().stream().reduce((a, b) -> b).get().getKey().getValue().get());
        cache.put(new AbstractMap.SimpleEntry<>(Instant.now(), value::getName), value);
        return value;
    }

    @Override
    public Optional<CurrentData> get(String key) {
        return cache.entrySet().stream().filter(entry ->
                entry.getKey().getValue().get().equals(key)).map(Map.Entry::getValue).findFirst();
    }

    @Override
    public Optional<CurrentData> clear(String key) {
        return cache.keySet().stream().filter(x -> x.getValue().get().equals(key)).findFirst().map(cache::remove);
    }

    @Override
    public Collection<String> clearAll() {
        return cache.keySet().stream()
                .map(Map.Entry::getValue)
                .map(Supplier::get)
                .map(this::clear)
                .map(Optional::orElseThrow)
                .map(CurrentData::getName)
                .toList();
    }
}
