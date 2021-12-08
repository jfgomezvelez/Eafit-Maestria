package co.edu.eafit;

import co.edu.eafit.mongodb.ProcessData;
import co.edu.eafit.mongodb.StatisticRepository;
import co.edu.eafit.statistic.FeatureType;
import co.edu.eafit.statistic.ProcessType;
import lombok.extern.java.Log;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

@RestController
@Log
@RequestMapping(value = "/api")
public class WeatherController {

    @Value("${visualcrossing.url}")
    private String url;

    @Value("${visualcrossing.key}")
    private String key;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/weather/{location}")
    public String getWeather(@PathVariable String location) {
        return checkWeather(location);
    }

    public String checkWeather(String location) {

        ProcessData process = new ProcessData(
                LocalTime.now(),
                ProcessType.CHECKWEATHER.toString(),
                FeatureType.BASICRabbitMQMessagePattern.toString()
        );

        log.info("Enviando evento "
                .concat(location)
                .concat(" a ").concat("weather.response.exchange")
                .concat(":")
                .concat("weather.response ")
                .concat("messageId")
                .concat(process.getId())
        );

        String data1 = "{\"location\":\"" + location + "\"}";
        byte[] data = data1.getBytes(StandardCharsets.UTF_8);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setMessageId(process.getId());

        Message message = new Message(data, messageProperties);

        rabbitTemplate.convertAndSend("weather.request.exchange", "weather.request", message);

        statisticRepository.save(process);

        return "ok";
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(
            value = "weather.response.queue"
    ),
            exchange = @Exchange(value = "weather.response.exchange", type = ExchangeTypes.TOPIC),
            key = "weather.response")
    )
    public void receive(Message message) {
        String data = new String(message.getBody());
        log.info("Recibiendo mensaje ".concat(data).concat(" messageId ").concat(message.getMessageProperties().getMessageId()));
        String result = statisticRepository.findById(message.getMessageProperties().getMessageId())
                .map(processData -> {
                    processData.setFinishDate(LocalTime.now());
                    processData.setDataSize(data.length());
                    statisticRepository.save(processData);
                    return "process actualizado";
                })
                .orElse("No existe messageId");
        log.info(result);
    }
}
