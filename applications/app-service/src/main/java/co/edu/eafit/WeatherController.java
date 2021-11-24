package co.edu.eafit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class WeatherController {

    private final CheckWeatherUseCase checkWeatherUseCase;

    @GetMapping("/weather/{location}")
    public Mono<ResponseEntity<String>> getWeather(@PathVariable String location) {
        return checkWeatherUseCase.checkWeather(location)
                .map(weather -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(weather));
    }
}
