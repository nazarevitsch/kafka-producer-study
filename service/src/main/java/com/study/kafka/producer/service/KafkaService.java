package com.study.kafka.producer.service;

import com.kafka.study.producer.client.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final static String RANDOMIZE_KEY = "random-key";

    private final static List<String> keys = List.of("Lamborghini", "Nissan", "Fiat", "Dodge", "Chevrolet",
            "Ferrari", "BMW", "Opel", "Porsche", "Bugatti", "Mercedes-Benz", "Seat", "Ford", "Audi", "Honda", "Lexus",
            "Acura", "KIA", "Jaguar", "Volkswagen", "Mazda", "Maserati", "Jeep", "Toyota", "Cadillac", "Subaru",
            "Alfa Romeo", "Mitsubishi", "Renault", "Peugeot");

    private final static List<String> states = List.of("Alabama", "Alaska", "Arizona", "Arkansas", "California",
            "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
            "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
            "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "Wyoming",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
            "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin");

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Value("${pod.id}")
    private Integer podId;

    public void sendMessages(String key, Integer count) {
        log.info("Start time: {}", new Date());
        if (RANDOMIZE_KEY.equals(key)) {
            for (int i = 0; i < count; i++) {
                String messageKey = randomKey();
                ProducerRecord<String, Message> record = new ProducerRecord<>(topicName, messageKey, new Message(i, podId, messageKey, randomState(), randomPrice()));
                Future<SendResult<String, Message>> future = kafkaTemplate.send(record);
//                logAfterSending(future);
            }
        } else {
            for (int i = 0; i < count; i++) {
                ProducerRecord<String, Message> record = new ProducerRecord<>(topicName, new Message(i, podId, randomKey(), randomState(), randomPrice()));
                Future<SendResult<String, Message>> future = kafkaTemplate.send(record);
//                logAfterSending(future);
            }
        }
        log.info("Finish time: {}", new Date());
    }

    private static void logAfterSending(Future<SendResult<String, Message>> future) {
        try {
            SendResult<String, Message> result = future.get();
            log.info("Send int partition: {}, with key: {}, message: {}", result.getRecordMetadata().partition(), result.getProducerRecord().key(), result.getProducerRecord().value());
        } catch (Exception e) {
            log.warn("Error occurred !!!");
        }
    }

    private static String randomKey() {
        return keys.get((int) (Math.random() * keys.size()));
    }

    private static String randomState() {
        return states.get((int) (Math.random() * states.size()));
    }

    private static int randomPrice() {
        return (int) (Math.random() * 80000) + 20000;
    }
}
