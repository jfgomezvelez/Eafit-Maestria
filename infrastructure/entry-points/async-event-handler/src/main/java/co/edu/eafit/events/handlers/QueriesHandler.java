package co.edu.eafit.events.handlers;

import co.edu.eafit.events.WeatherQuery;
import co.edu.eafit.model.weather.Weather;
import co.edu.eafit.usecase.CheckWeatherUseCase;
import com.sun.org.apache.xpath.internal.axes.WalkerFactory;
import lombok.AllArgsConstructor;
import org.reactivecommons.async.impl.config.annotations.EnableQueryListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableQueryListeners
public class QueriesHandler {

    private final CheckWeatherUseCase checkWeatherUseCase;

    public Mono<Weather> handleQueryA(WeatherQuery query) {
        System.out.println("query received->" + query.getLocation()); // TODO: Remove this line
//        return sampleUseCase.doSomethingReturningNoMonoVoid(query);
        return checkWeatherUseCase.checkWeather(query.getLocation());
    }
}
