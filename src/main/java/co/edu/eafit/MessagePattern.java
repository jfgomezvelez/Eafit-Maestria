package co.edu.eafit;

import co.edu.eafit.mongodb.StatisticRepository;
import com.rabbitmq.client.Channel;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Locale;

@Log
@Component
public class MessagePattern {
    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode}")
    private String mode;

    @RabbitListener(queues = "weather.service.em.queue", concurrency = "${process.concurrency}")
    public void receive(Message message, Channel channel) {
        String data = new String(message.getBody());
        log.info("Recibiendo mensaje ".concat(data).concat(" messageId ").concat(message.getMessageProperties().getMessageId()));
        boolean result = statisticRepository.findById(message.getMessageProperties().getMessageId())
                .map(processData -> {
                    processData.setFinishDate(LocalTime.now());
                    processData.setDataSize(data.length());
                    statisticRepository.save(processData);
                    return true;
                })
                .orElse(false);
        log.info("resultado de la actualizacion: " + result);

        if("manual".toUpperCase(Locale.ROOT).equals(mode.toUpperCase())){
            try {
                if (result) {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } else {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Integer messageType, String id) {

        log.info("Enviando evento "
                .concat(String.valueOf(messageType))
                .concat(" a ").concat("weather.service.cm.exchange  ")
                .concat(": ")
                .concat("weather.service.queue ")
                .concat("messageId")
                .concat(id));


        String data1 = "{\"messageType\": " + messageType + "}";
        byte[] data = data1.getBytes(StandardCharsets.UTF_8);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setMessageId(id);

        Message message = new Message(data, messageProperties);

        rabbitTemplate.convertAndSend("weather.api.em.exchange ", message);
    }
}
