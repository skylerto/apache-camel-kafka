package me.skylerlayne.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaCamelRoute {

    @Bean(name = "KafkaRouteProducer")
    public RouteBuilder kafkaRouteProducer() {
        return new KafkaRouteProducer(
                "kafka:localhost:9092?topic=test&groupId=testing&autoOffsetReset=earliest&consumersCount=1");
    }

    @Bean(name = "KafkaRouteConsumer")
    public RouteBuilder kafkaRouteConsumer() {
        return new RouteBuilder() {
            public void configure() {
                from("kafka:localhost:9092?topic=test&groupId=testing&autoOffsetReset=earliest&consumersCount=1")
                        .bean(KafkaOutputBean.class);
            }
        };
    }
}