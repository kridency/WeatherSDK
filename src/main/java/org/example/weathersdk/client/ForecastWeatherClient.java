package org.example.weathersdk.client;

import lombok.Builder;
import lombok.SneakyThrows;
import org.example.weathersdk.constant.*;
import org.example.weathersdk.dto.WeatherData;
import org.example.weathersdk.dto.Forecast;
import org.example.weathersdk.dto.ForecastData;
import org.example.weathersdk.service.ForecastDataCacheService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ForecastWeatherClient extends AbstractClient<ForecastData> {
    private String apiKey;
    private Country country;
    private Unit unit;
    private ResponseMode mode;
    private Integer cnt;

    @SneakyThrows
    @Builder
    private ForecastWeatherClient(String apiKey,
                                  Country country,
                                  Unit unit,
                                  ResponseMode responseMode,
                                  Integer cnt,
                                  String mode) {
        var key = Optional.ofNullable(apiKey)
                .orElseThrow(() -> new IllegalArgumentException("No API Key is specified!"));

        INSTANCES.merge(key, this, (a, b) -> {
            throw new IllegalArgumentException("An object with the same key already exists!");
        });

        serviceURL = Endpoint.DATA_URL + "/2.5" + Endpoint.FORECAST
                + "?appid=" + key
                + Optional.ofNullable(country).map(x -> "&lang=" + x).orElse("")
                + Optional.ofNullable(unit).map(x -> "&units=" + x).orElse("")
                + Optional.ofNullable(responseMode).map(x -> "&mode=" + x).orElse("")
                + Optional.ofNullable(cnt).map(x -> "&cnt=" + x).orElse("");

        nameURL = Endpoint.GEO_URL + "/1.0" + Endpoint.CITY_NAME + "?appid=" + key;

        var cachingMode = Optional.ofNullable(mode).map(CachingMode::valueOf).orElse(CachingMode.ON_DEMAND);

        if (cachingMode == CachingMode.POLLING) {
            scheduler.scheduleAtFixedRate(cacheService::refreshCache, 0, 600, TimeUnit.SECONDS);
        }

        cacheService = new ForecastDataCacheService(this, cachingMode);
    }

    /**
     * Converts the weather service data to the library data representation.
     * The auxiliary function for receiving weather data.
     * @param description    City name, state code(for US only) and country code
     *                       according to ISO 3166 country codes
     *
     * @return  converted weather data object
     */
    private WeatherData getObject(String... description) {
        var len = description.length;
        if (len == 0) throw new IllegalArgumentException("No city name is specified!");

        try {
            var forecastData = cacheService.getRawDataObject(x -> Objects.requireNonNull(x.getList()
                    .stream().findFirst().map(Forecast::getDt).orElse(null)), description);

            var forecast = forecastData.getList().stream().findFirst();

            return WeatherData.builder()
                    .weather(forecast.flatMap(x -> x.getWeather().stream().findFirst()).orElse(null))
                    .temperature(forecast.map(Forecast::getMain).orElse(null))
                    .visibility(forecast.map(Forecast::getVisibility).orElse(0))
                    .wind(forecast.map(Forecast::getWind).orElse(null))
                    .datetime(forecast.map(Forecast::getDt).orElse(null))
                    .sys(forecast.map(Forecast::getSys).orElse(null))
                    .timezone(forecastData.getCity().getTimezone().getRawOffset())
                    .name(forecastData.getCity().getName())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Converts the library weather data representation to a JSON string.
     * The main function for receiving weather data.
     * @param description    City name, state code(for US only) and country code
     *                       according to ISO 3166 country codes
     *
     * @return  JSON string weather data
     */
    public String getJSON(String... description) throws IOException {
        return objectMapper.writeValueAsString(getObject(description));
    }
}
