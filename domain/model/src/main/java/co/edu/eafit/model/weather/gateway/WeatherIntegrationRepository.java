package co.edu.eafit.model.weather.gateway;

import co.edu.eafit.model.weather.Weather;
import reactor.core.publisher.Mono;

public interface WeatherIntegrationRepository {

    Mono<Weather> requestWeather(Weather weather, String processId);
}
