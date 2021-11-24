package co.edu.eafit.events.dto;

import co.edu.eafit.model.weather.Weather;
import lombok.Data;

@Data
public class WeatherCommandResponseDTO {
    private Weather weather;
    private String processId;
}
