package me.skylerlayne.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/message")
public class MessageController {

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

	/**
	 * GET to produce a message for Kafaka.
	 * 
	 * @param request
	 *            - the {@link HttpServletRequest} object.
	 * @param response
	 *            - the {@link HttpServletResponse} object.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void get(HttpServletRequest request, HttpServletResponse response) {
		try {
			camelContext.addRoutes(kafkaRouteProducer);
			camelContext.addRoutes(kafkaRouteConsumer);

			kafkaProducer.sendBody("direct:kafkaRoute", "testKafkaMessage");

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
