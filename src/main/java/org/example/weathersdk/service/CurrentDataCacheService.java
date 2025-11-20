package org.example.weathersdk.service;

import org.example.weathersdk.client.CurrentWeatherClient;
import org.example.weathersdk.constant.CachingMode;
import org.example.weathersdk.dto.CurrentData;
import org.example.weathersdk.repository.CurrentDataRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

public class CurrentDataCacheService implements CacheService<CurrentData> {
    private final CurrentDataRepository cacheRepository = new CurrentDataRepository();
    private final CurrentWeatherClient currentWeatherClient;
    private final CachingMode cachingMode;

    public CurrentDataCacheService(CurrentWeatherClient currentWeatherClient, CachingMode cachingMode) {
        this.currentWeatherClient = currentWeatherClient;
        this.cachingMode = cachingMode;
    }

    /**
     * Check cache if the weather data is already stored and if not adds it to.
     * The main function for managing weather data stored in the cache.
     * @param function      functional interface used to retrieve weather data datetime
     * @param description    City name, state code(for US only) and country code
     *                       according to ISO 3166 country codes
     *
     * @return  weather data object
     */
    public CurrentData getRawDataObject(Function<CurrentData, Instant> function, String... description) throws IOException {
        return cacheRepository.get(description[0]).map(x -> {
            if (cachingMode.equals(CachingMode.ON_DEMAND) && Duration.between(function.apply(x), Instant.now()).toMinutes() >= 10L) {
                try {
                    return refreshCache(description);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            return x;
        }).orElse(cacheRepository.put(currentWeatherClient.getObjectMapper().convertValue(currentWeatherClient
                .getWeatherObject(description), CurrentData.class)));
    }

    /**
     * Refreshes the cached weather data for a specified location.
     * The main function for refreshing the cached weather data of a given city description.
     * @param description    City name, state code(for US only) and country code
     *                       according to ISO 3166 country codes
     *
     * @return  weather data object
     */
    public CurrentData refreshCache(String... description) throws IOException {
        var len = description.length;
        if (len == 0) throw new IllegalArgumentException("No city name is specified!");
        cacheRepository.clear(description[0]);
        return cacheRepository.put(currentWeatherClient.getObjectMapper().convertValue(currentWeatherClient
                .getWeatherObject(description), CurrentData.class));
    }

    /**
     * Refreshes cached weather data for all stored locations.
     * The main function for refreshing cached weather data of all stored locations.
     *
     */
    public void refreshCache() {
        cacheRepository.clearAll().forEach(x -> {
            try {
                refreshCache(x);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}
