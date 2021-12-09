package co.edu.eafit;

import co.edu.eafit.mongodb.ProcessData;
import co.edu.eafit.mongodb.StatisticRepository;
import co.edu.eafit.statistic.FeatureType;
import co.edu.eafit.statistic.ProcessType;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;


@RestController
@Log
@RequestMapping(value = "/api")
public class WeatherController {

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private MessagePattern messagePattern;

    @GetMapping("/weather/{location}")
    public String getWeather(@PathVariable String location) {

        ProcessData process = new ProcessData(
                LocalTime.now(),
                ProcessType.CHECKWEATHER.toString(),
                FeatureType.BASICRabbitMQMessagePattern.toString()
        );

        messagePattern.send(location, process.getId());

        statisticRepository.save(process);

        return "requested weather";
    }
}
