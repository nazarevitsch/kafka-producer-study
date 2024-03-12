package com.study.kafka.producer.controller;

import com.study.kafka.producer.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/kafka/producer")
public class KafkaController {

    private final KafkaService kafkaService;

    @PostMapping()
    public ResponseEntity sendMessages(@RequestParam(required = false) String key,
                                       @RequestParam Integer count) {
        this.kafkaService.sendMessages(key, count);
        return ResponseEntity.noContent().build();
    }
}
