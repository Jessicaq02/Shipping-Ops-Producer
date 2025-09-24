package com.logistics.producer.api;

import com.logistics.events.ShipmentEvent;
import com.logistics.producer.publisher.KafkaEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private KafkaEventPublisher publisher;

    @PostMapping
    public Mono<ResponseEntity<String>> createShipment(@RequestBody ShipmentEvent event){
        if (event.getShipmentId() == null || event.getShipmentId().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body("shipmentId es obligatorio"));
        }

        return publisher.publishEvent(event)
                .map(eventId -> ResponseEntity.accepted().body(eventId));
    }
}
