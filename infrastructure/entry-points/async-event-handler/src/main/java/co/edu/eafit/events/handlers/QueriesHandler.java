package co.edu.eafit.events.handlers;

import co.edu.eafit.events.WeatherQuery;
import co.edu.eafit.model.weather.Weather;
import com.sun.org.apache.xpath.internal.axes.WalkerFactory;
import lombok.AllArgsConstructor;
import org.reactivecommons.async.impl.config.annotations.EnableQueryListeners;
import reactor.core.publisher.Mono;

//@AllArgsConstructor
//@EnableQueryListeners
public class QueriesHandler {
//    private final SampleUseCase sampleUseCase;


    public Mono<Weather> handleQueryA(WeatherQuery location) {
        System.out.println("query received->" + location); // TODO: Remove this line
//        return sampleUseCase.doSomethingReturningNoMonoVoid(query);
        return Mono.just(Weather.builder().build());
    }
}
