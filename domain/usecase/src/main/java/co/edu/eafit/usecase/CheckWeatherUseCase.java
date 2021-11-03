package co.edu.eafit.usecase;

import co.edu.eafit.model.Weather;
import co.edu.eafit.model.gateway.WeatherRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CheckWeatherUseCase {

    private final WeatherRepository weatherRepository;

    public Mono<Weather> checkWeather(String location){
        return weatherRepository.checkWeather(location);
    }
}
