package co.edu.eafit.model.gateway;

import co.edu.eafit.model.Weather;
import reactor.core.publisher.Mono;

public interface WeatherRepository {

    Mono<Weather> checkWeather(String location);
}
