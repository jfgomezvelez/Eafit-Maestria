package co.edu.eafit;

import co.edu.eafit.mongodb.ProcessData;
import co.edu.eafit.mongodb.StatisticRepository;
import co.edu.eafit.statistic.FeatureType;
import co.edu.eafit.statistic.ProcessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class WeatherController {

    private final VisualCrossingWeather visualCrossingWeather;
    private final StatisticRepository statisticRepository;

    @GetMapping("/weather/{location}")
    public Mono<ResponseEntity<String>> getWeather(@PathVariable String location) {
        return checkWeather(location)
                .map(weather -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(weather));
    }

    public Mono<String> checkWeather(String location) {

        ProcessData process = new ProcessData();
        process.setInitialDate(LocalTime.now());
        process.setId(UUID.randomUUID().toString());
        process.setTraceabilityIdentifier(UUID.randomUUID().toString());
        process.setName(ProcessType.CHECKWEATHER.toString());
        process.setFeature(FeatureType.ONLYHTTP.toString());

        return visualCrossingWeather.checkWeather(location)
                .flatMap(weather -> {
                    process.setFinishDate(LocalTime.now());
                    process.setDataSize(weather.length());
                    return statisticRepository.save(process).map(result -> weather);
                });
    }
}
