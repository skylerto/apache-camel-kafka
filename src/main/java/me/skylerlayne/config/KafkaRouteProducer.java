package me.skylerlayne.config;

import org.apache.camel.builder.RouteBuilder;

public class KafkaRouteProducer extends RouteBuilder {

	private String route;

	public KafkaRouteProducer(String route) {
		this.route = route;
	}

	public void configure() {
		from("direct:kafkaRoute").to(this.route);
	}
}
