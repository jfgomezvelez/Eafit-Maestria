package co.edu.eafit;

import co.edu.eafit.mongodb.ProcessData;
import co.edu.eafit.mongodb.StatisticRepository;
import co.edu.eafit.statistic.FeatureType;
import co.edu.eafit.statistic.ProcessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.UUID;

@RestController

@RequestMapping(value = "/api")
public class WeatherController {

    @Autowired
    private VisualCrossingWeather visualCrossingWeather;
    @Autowired
    private StatisticRepository statisticRepository;

    @GetMapping("/weather/{location}")
    public String getWeather(@PathVariable String location) {
        return checkWeather(location);
    }

    public String checkWeather(String location) {

        ProcessData process = new ProcessData();
        process.setInitialDate(LocalTime.now());
        process.setId(UUID.randomUUID().toString());
        process.setTraceabilityIdentifier(UUID.randomUUID().toString());
        process.setName(ProcessType.CHECKWEATHER.toString());
        process.setFeature(FeatureType.BASICONLYHTTP.toString());

        String weather = visualCrossingWeather.checkWeather(location);

        process.setFinishDate(LocalTime.now());
        process.setDataSize(weather.length());

        statisticRepository.save(process);

        return weather;
    }
}
