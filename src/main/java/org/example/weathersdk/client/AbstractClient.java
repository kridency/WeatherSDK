package org.example.weathersdk.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.weathersdk.dto.Coord;
import org.example.weathersdk.dto.Location;
import org.example.weathersdk.service.CacheService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class AbstractClient<T> {
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected String nameURL;
    protected String serviceURL;
    protected static final Map<String, Object> INSTANCES = new HashMap<>();
    protected CacheService<T> cacheService;
    protected ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public ObjectMapper getObjectMapper() { return objectMapper; }

    /**
     * Requests openweathermap.org for a JSON represented content.
     * The auxiliary function for the JSON weather data and the geolocation requests.
     * @param url    URL object to request openweathermap.org
     *
     * @return  response with weather data
     */
    private String getContent(URL url) throws IOException {
        String line;
        var content = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) content.append(line).append("\n");
            connection.disconnect();
        } else {
            throw new IOException(connection.getResponseMessage());
        }

        return content.toString().replaceAll("<[^>]*>", "").strip();
    }

    /**
     * Requests city location.
     * The auxiliary function for acquiring a JSON city coordinates.
     * @param cityName    City name
     * @param stateCode    State code for US
     * @param countryCode    Country code according to ISO 3166
     *
     * @return  response with city coordinates
     */
    private Coord getCoords(String cityName, String stateCode, String countryCode) throws IOException, RuntimeJsonMappingException {
        var url = getURL(nameURL + "&limit=1&q=" + cityName + stateCode + countryCode);
        var location = objectMapper.readValue(getContent(url), new TypeReference<Collection<Location>>() {})
                .stream().findFirst();
        var lat = location.map(Location::getLat).orElseThrow(() -> new RuntimeJsonMappingException("Wrong location format!"));
        var lon = location.map(Location::getLon).orElseThrow(() -> new RuntimeJsonMappingException("Wrong location format!"));

        return Coord.builder().lat(lat).lon(lon).build();
    }

    /**
     * Converts link string to an URL object.
     * The auxiliary function for http link conversion.
     * @param link    HTTP link
     *
     * @return  URL object
     */
    private URL getURL(String link) throws MalformedURLException {
        return URL.of(URI.create(link), null);
    }

    /**
     * Converts the JSON formatted string to a weather data object.
     * The main function for retrieving the weather data of given coordinates.
     * @param lat    Earth location latitude
     * @param lon    Earth location longitude
     *
     * @return  weather data object
     */
    protected T getWeatherObject(float lat, float lon) throws IOException {
        var url = getURL(serviceURL + "&lat=" + lat + "&lon=" + lon);
        var line = getContent(url);
        return line.isEmpty() ? null : objectMapper.readValue(line, new TypeReference<>() {});
    }

    /**
     * Converts the JSON formatted string to a weather data object.
     * The main function for retrieving the weather data of a given city description.
     * @param description    City name, state code(for US only) and country code
     *                       according to ISO 3166 country codes
     *
     * @return  weather data object
     */
    public T getWeatherObject(String... description) throws IOException, RuntimeJsonMappingException {
        var len = description.length;
        if (len == 0) throw new IllegalArgumentException("No city name is specified!");

        var cityName = Optional.of(description[0]).map(String::trim).orElse("");
        var countryCode = len > 1 ? Optional.of(description[len - 1]).map(x ->
                (x.trim().isEmpty() ? "" : ",") + x.trim()).orElse("") : "";
        var stateCode = len == 3 && countryCode.equals("US") ? Optional.of(description[1]).map(x ->
                (x.trim().isEmpty() ? "" : ",") + x.trim()).orElse("") : "";

        var coords = getCoords(cityName, stateCode, countryCode);
        return coords == null ? null : getWeatherObject(coords.getLat(), coords.getLon());
    }

    /**
     * Deletes weather data object.
     * The main function for deleting existing weather data object.
     * @param apiKey    Customer API Key
     *
     */
    public static void delete(String apiKey) {
        INSTANCES.remove(apiKey);
    }
}
