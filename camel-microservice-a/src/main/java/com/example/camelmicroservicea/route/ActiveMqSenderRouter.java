package com.example.camelmicroservicea.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
//        from("timer:active-mq-timer?period=10000")
//                .transform().constant("My message for Active MQ")
//                .log("${body}")
//                .to("activemq:my-activemq-queue");

        from("file:json")
                .log("${body}")
                .to("activemq:my-activemq-queue");
    }
}
