package org.example.weathersdk.client;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.example.weathersdk.constant.*;
import org.example.weathersdk.dto.CurrentData;
import org.example.weathersdk.dto.WeatherData;
import org.example.weathersdk.service.CurrentDataCacheService;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Getter
public class CurrentWeatherClient extends AbstractClient<CurrentData> {
    private String apiKey;
    private Country country;
    private Unit unit;
    private ResponseMode responseMode;

    @SneakyThrows
    @Builder
    private CurrentWeatherClient(String apiKey, Country country, Unit unit, ResponseMode responseMode, String mode) {
        var key = Optional.ofNullable(apiKey)
                .orElseThrow(() -> new IllegalArgumentException("No API Key is specified!"));

        INSTANCES.merge(key, this, (a, b) -> {
            throw new IllegalArgumentException("An object with the same key already exists!");
        });

        serviceURL = Endpoint.DATA_URL + "/2.5" + Endpoint.CURRENT
                + "?appid=" + Optional.ofNullable(key)
                .orElseThrow(() -> new IllegalArgumentException("No API Key is specified!"))
                + Optional.ofNullable(country).map(x -> "&lang=" + x).orElse("")
                + Optional.ofNullable(unit).map(x -> "&units=" + x).orElse("")
                + Optional.ofNullable(responseMode).map(x -> "&mode=" + x).orElse("");

        nameURL = Endpoint.GEO_URL + "/1.0" + Endpoint.CITY_NAME + "?appid=" + key;

        var cachingMode = Optional.ofNullable(mode).map(CachingMode::valueOf).orElse(CachingMode.ON_DEMAND);

        if (cachingMode == CachingMode.POLLING) {
            scheduler.scheduleAtFixedRate(cacheService::refreshCache, 0, 600, TimeUnit.SECONDS);
        }

        cacheService = new CurrentDataCacheService(this, cachingMode);
    }

    /**
     * Converts the weather service data to the library data representation.
     * The main function for receiving weather data.
     * @param description    City name, state code(for US only) and country code
     *                       according to ISO 3166 country codes
     *
     * @return  converted weather data object
     */
    private WeatherData getObject(String... description) {
        var len = description.length;
        if (len == 0) throw new IllegalArgumentException("No city name is specified!");

        try {
            var currentData = cacheService.getRawDataObject(CurrentData::getDt, description);

            return WeatherData.builder()
                    .weather(currentData.getWeather().stream().findFirst().orElse(null))
                    .temperature(currentData.getMain())
                    .visibility(currentData.getVisibility())
                    .wind(currentData.getWind())
                    .datetime(currentData.getDt())
                    .sys(currentData.getSys())
                    .timezone(currentData.getTimezone().getRawOffset())
                    .name(currentData.getName())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getJSON(String... description) throws IOException {
        return objectMapper.writeValueAsString(getObject(description));
    }
}
