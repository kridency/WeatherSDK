package org.example.weathersdk.service;

import org.example.weathersdk.client.ForecastWeatherClient;
import org.example.weathersdk.constant.CachingMode;
import org.example.weathersdk.dto.ForecastData;
import org.example.weathersdk.repository.ForecastDataRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

public class ForecastDataCacheService implements CacheService<ForecastData> {
    private final ForecastDataRepository forecastCacheRepository = new ForecastDataRepository();
    private final ForecastWeatherClient forecastWeatherClient;
    private final CachingMode cachingMode;

    public ForecastDataCacheService(ForecastWeatherClient forecastWeatherClient, CachingMode cachingMode) {
        this.forecastWeatherClient = forecastWeatherClient;
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
    public ForecastData getRawDataObject(Function<ForecastData, Instant> function, String... description) throws IOException {
        return forecastCacheRepository.get(description[0]).map(x -> {
            if (cachingMode.equals(CachingMode.ON_DEMAND) && Duration.between(function.apply(x), Instant.now()).toMinutes() >= 10L) {
                try {
                    return refreshCache(description);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            return x;
        }).orElse(forecastCacheRepository.put(forecastWeatherClient.getObjectMapper().convertValue(forecastWeatherClient
                .getWeatherObject(description), ForecastData.class)));
    }

    /**
     * Refreshes the cached weather data for a specified location.
     * The main function for refreshing the cached weather data of a given city description.
     * @param description    City name, state code(for US only) and country code
     *                       according to ISO 3166 country codes
     *
     * @return  weather data object
     */
    public ForecastData refreshCache(String... description) throws IOException {
        var len = description.length;
        if (len == 0) throw new IllegalArgumentException("No city name is specified!");
        forecastCacheRepository.clear(description[0]);
        return forecastCacheRepository.put(forecastWeatherClient.getObjectMapper().convertValue(forecastWeatherClient
                .getWeatherObject(description), ForecastData.class));
    }

    /**
     * Refreshes cached weather data for all stored locations.
     * The main function for refreshing cached weather data of all stored locations.
     *
     */
    public void refreshCache() {
        forecastCacheRepository.clearAll().forEach(x -> {
            try {
                refreshCache(x);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}
