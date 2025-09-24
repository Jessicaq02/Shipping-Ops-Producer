package com.logistics.producer.publisher;

import com.logistics.events.ShipmentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Service

public class KafkaEventPublisher {
    private static final String TOPIC = "logistics.shipments.v1";
    private static final Duration TTL = Duration.ofHours(4);

    @Autowired
    private KafkaTemplate<String, ShipmentEvent> KafkaTemplate;
    @Autowired
    private ReactiveRedisOperations<String, ShipmentEvent> redisOperations;

    public Mono<String> publishEvent(ShipmentEvent event){
        String eventId = UUID.randomUUID().toString();
        event.setEventId(eventId);

        String redisKey = "ship:event:" + event.getShipmentId();
        Mono<Boolean> redisSave = redisOperations
                .opsForValue()
                .set(redisKey, event, TTL);
        KafkaTemplate.send(TOPIC, event.getShipmentId(), event);

        return redisSave
                .thenReturn(eventId);
    }


}
