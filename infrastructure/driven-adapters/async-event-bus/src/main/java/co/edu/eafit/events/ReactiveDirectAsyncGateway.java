package co.edu.eafit.events;

import co.edu.eafit.events.dto.WeatherCommandResponseDTO;
import co.edu.eafit.model.weather.Weather;
import co.edu.eafit.model.weather.gateway.WeatherIntegrationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Level;

@Log
@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements WeatherIntegrationRepository {
    public static final String TARGET_NAME = "weather-processing";// refers to remote spring.application.name property
    public static final String SOME_COMMAND_NAME = "weather.response";

    private final DirectAsyncGateway gateway;

    @Override
    public Mono<Weather> requestWeather(Weather weather, String processId) {
        log.log(Level.INFO, "Sending command: {0}: {1}", new String[]{SOME_COMMAND_NAME, weather.toString()});
        WeatherCommandResponseDTO weatherCommandResponseDTO= new WeatherCommandResponseDTO();
        weatherCommandResponseDTO.setWeather(weather);
        weatherCommandResponseDTO.setProcessId(processId);
        return gateway.sendCommand(new Command<>(SOME_COMMAND_NAME, UUID.randomUUID().toString(), weatherCommandResponseDTO),
                TARGET_NAME).thenReturn(weather);
    }
}
