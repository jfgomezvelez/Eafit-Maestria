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
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

@Log
@Component
public class MessagePattern {

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(
            value = "weather.response.queue"
    ),
            exchange = @Exchange(value = "weather.exchange", type = ExchangeTypes.TOPIC),
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

    public void send(String location) {

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

        rabbitTemplate.convertAndSend("weather.exchange", "weather.request", message);

        statisticRepository.save(process);
    }
}
