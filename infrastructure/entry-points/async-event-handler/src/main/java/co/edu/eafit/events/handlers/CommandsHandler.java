package co.edu.eafit.events.handlers;

import co.edu.eafit.events.WeatherCommand;
import co.edu.eafit.usecase.CheckWeatherUseCase;
import co.edu.eafit.usecase.RequestWeatherUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {

    private final RequestWeatherUseCase requestWeatherUseCase;

    public Mono<Void> handleCommandA(Command<WeatherCommand> command) {
        System.out.println("command received: " + command.getName() + " ->" + command.getData()); // TODO: Remove this line
        return requestWeatherUseCase.requestWeather(command.getData().getLocation(), command.getData().getProcessId()).then();
    }
}
