package co.edu.eafit;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Log
@RequestMapping(value = "/api")
public class WeatherController {

    @Autowired
    private MessagePattern messagePattern;

    @GetMapping("/weather/{location}")
    public String getWeather(@PathVariable String location) {
        messagePattern.send(location);
        return "requested weather";
    }
}
