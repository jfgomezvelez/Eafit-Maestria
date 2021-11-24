package co.edu.eafit;

import co.edu.eafit.mongodb.ProcessData;
import co.edu.eafit.mongodb.StatisticRepository;
import co.edu.eafit.statistic.FeatureType;
import co.edu.eafit.statistic.ProcessType;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.UUID;

@RequiredArgsConstructor
public class CheckWeatherUseCase {

    private final VisualCrossingWeather visualCrossingWeather;
    private final StatisticRepository statisticRepository;

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
