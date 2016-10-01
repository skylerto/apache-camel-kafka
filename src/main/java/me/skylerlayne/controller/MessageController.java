package me.skylerlayne.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.skylerlayne.model.Notification;

@Controller
@ResponseBody
@RequestMapping("/message")
public class MessageController {

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	CamelContext camelContext;

	@Autowired
	@Qualifier("KafkaRouteProducer")
	RouteBuilder kafkaRouteProducer;

	@Autowired
	@Qualifier("KafkaRouteConsumer")
	RouteBuilder kafkaRouteConsumer;

	@EndpointInject(uri = "direct:kafkaRoute")
	ProducerTemplate kafkaProducer;

	ConsumerTemplate kafkaConsumer;

	@PostConstruct
	public void setup() {
		try {
			camelContext.addRoutes(kafkaRouteProducer);
			camelContext.addRoutes(kafkaRouteConsumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * GET to produce a message for Kafaka.
	 * 
	 * @param request
	 *            the {@link HttpServletRequest} object.
	 * @param response
	 *            the {@link HttpServletResponse} object.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void get(HttpServletRequest request, HttpServletResponse response) {
		try {
			kafkaProducer.sendBody("direct:kafkaRoute", "This is a message from the /message route!");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * POST a notification, send it as a message to Kafka.
	 * 
	 * @param request
	 *            the {@link HttpServletRequest} object.
	 * @param response
	 *            the {@link HttpServletResponse} object.
	 * @param notification
	 *            the {@link Notification} to be posted.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void post(HttpServletRequest request, HttpServletResponse response, @RequestBody Notification notification) {
		try {
			kafkaProducer.sendBody("direct:kafkaRoute", mapper.writeValueAsString(notification));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
