package co.edu.eafit.visualcrossingweather.adapters;

import co.edu.eafit.model.Weather;
import co.edu.eafit.model.gateway.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Repository
@RequiredArgsConstructor
@Log
public class WeatherAdapter implements WeatherRepository {

    private final WebClient webClient;

    @Value("${visualcrossing.url}")
    private String url;

    @Value("${visualcrossing.key}")
    private String key;

    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";

    @Override
    public Mono<Weather> checkWeather(String location) {
        return webClient
                .get()
                .uri(createURL(location))
                .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    log.info("Respuesta ".concat(String.valueOf(clientResponse.statusCode().value()))
                            .concat(" ")
                            .concat(clientResponse.statusCode().getReasonPhrase()));
                    if (clientResponse.statusCode().is2xxSuccessful())
                        return clientResponse.bodyToMono(Weather.class);
                    clientResponse.body((clientHttpResponse, context) -> {
                        return clientHttpResponse.getBody();
                    });
                    return clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new Exception(error)));
                })
                .onErrorResume(error -> {
                    log.info("Error ".concat(error.getMessage()));
                    return Mono.error(error);
                });
    }

    private URI createURL(String location) {
        return UriComponentsBuilder
                .fromUriString(url.concat(location).concat("?unitGroup=metric&key=").concat(key))
                .build()
                .toUri();
    }
}
