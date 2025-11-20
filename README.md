# Simple Weather SDK

Library that can be used by other developers to easily access a `openweathermap.org` API and retrieve weather data for 
a given location.

## Requirements

- **Java 17 or higher**: Library requires Java 17 as the minimum runtime version
- **Maven 3.x or higher**: Library requires Maven 3.x+ for building from the source

## Features

- **Weather data in JSON format**: offers custom JSON weather data representation retrieved from `openweathermap.org`:
```json
{
"weather": {
"main": "Clouds",
"description": "scattered clouds",
},
"temperature": {
"temp": 269.6,
"feels_like": 267.57,
},
"visibility": 10000,
"wind": {
"speed": 1.38,
},
"datetime": 1675744800,
"sys": {
"sunrise": 1675751262,
"sunset": 1675787560
},
"timezone": 3600,
"name": "Zocca"
}
```
- **Weather data transfer object types**: offers type model to support object mapping

## Build & Install

Checkout sources from github repository:
```shell
# git clone https://github.com/kridency/WeatherSDK.git
```

In order to make package including dependencies run maven in the source directory:
```shell
# mvn package
```

Copy package `weather-sdk-1.0.0-SNAPSHOT.jar` to your project `/lib` directory and include dependency in your `pom.xml` to use objects and methods from the library:
```xml
<dependency>
   <groupId>com.example</groupId>
   <artifactId>weather-sdk</artifactId>
   <version>1.0.0</version>
   <scope>system</scope>
   <systemPath>${project.basedir}/lib/weather-sdk-1.0.0-SNAPSHOT.jar</systemPath>
   <type>jar</type>
</dependency>
```


## Usage instructions

Create `CurrentWeatherClient` client for your API Key:

```java
import org.example.weathersdk.client.CurrentWeatherClient;

var client = CurrentWeatherClient.builder().apiKey("#YOUR-API-KEY#").build();
```

Get JSON weather data representation for a given city name:
```java
var json = client.getJSON("#CITY-NAME#");
```   

## Library description:

1. Library includes capabilities responsible for managing the communication with `openweathermap.org`.

2. In order to minimize client response library includes the caching service. It is organized according to the chosen caching strategy (IN_DEMAND, POLLING).

3. Inner library logic exclude creating clients with the same API Key.

3. There are optional parameters while building the client:
```
|   Name  |                              Description                                   |
|---------|----------------------------------------------------------------------------|
| unit    | Units of measurement. standard, metric and imperial units are available.   |
|---------|----------------------------------------------------------------------------|
| country | You can use this parameter to get the output in your language.             |
```

