package co.edu.eafit;

import co.edu.eafit.mongodb.ProcessData;
import co.edu.eafit.mongodb.StatisticRepository;
import co.edu.eafit.statistic.FeatureType;
import co.edu.eafit.statistic.ProcessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.UUID;

@RestController

@RequestMapping(value = "/api")
public class WeatherController {

    @Value("${visualcrossing.url}")
    private String url;

    @Value("${visualcrossing.key}")
    private String key;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StatisticRepository statisticRepository;

    @GetMapping("/weather/{location}")
    public String getWeather(@PathVariable String location) {
        return checkWeather(location);
    }

    public String checkWeather(String location) {

        ProcessData process = new ProcessData(
                UUID.randomUUID().toString(),
                LocalTime.now(),
                ProcessType.CHECKWEATHER.toString(),
                UUID.randomUUID().toString(),
                FeatureType.BASICONLYHTTP.toString()
        );

        String weather =  restTemplate.getForObject(url.concat(location).concat("?unitGroup=metric&key=").concat(key), String.class);

        process.setFinishDate(LocalTime.now());
        process.setDataSize(weather.length());

        statisticRepository.save(process);

        return weather;
    }
}
