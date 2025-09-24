package com.logistics.producer.repo;

import com.logistics.events.ShipmentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisSnapshotRepository {
    @Bean
    public ReactiveRedisOperations<String, ShipmentEvent> redisOperations(
            ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<ShipmentEvent> serializer =
                new Jackson2JsonRedisSerializer<>(ShipmentEvent.class);
        RedisSerializationContext<String, ShipmentEvent> context =
                RedisSerializationContext.<String, ShipmentEvent>newSerializationContext(new StringRedisSerializer())
                        .value(serializer)
                        .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

}
