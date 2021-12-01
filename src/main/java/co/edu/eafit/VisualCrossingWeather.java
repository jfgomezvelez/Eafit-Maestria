package co.edu.eafit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class VisualCrossingWeather {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${visualcrossing.url}")
    private String url;

    @Value("${visualcrossing.key}")
    private String key;

    public String checkWeather(String location) {
        return restTemplate.getForObject(url.concat(location).concat("?unitGroup=metric&key=").concat(key), String.class);
    }
}
