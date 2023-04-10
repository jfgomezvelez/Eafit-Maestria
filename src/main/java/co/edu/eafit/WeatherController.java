package co.edu.eafit;

import co.edu.eafit.mongodb.ProcessData;
import co.edu.eafit.mongodb.StatisticRepository;
import co.edu.eafit.statistic.FeatureType;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@Log
@RequestMapping(value = "/service")
public class WeatherController {

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private MessagePattern messagePattern;

    @Value("${process.type}")
    private String processType;

    @GetMapping("/weather/{messageType}")
    public String getWeather(@PathVariable Integer messageType) {

        ProcessData process = new ProcessData(
                LocalTime.now(),
                processType,
                processType
        );

        statisticRepository.save(process);

        messagePattern.send(messageType, process.getId());

        return "requested weather";
    }
}
