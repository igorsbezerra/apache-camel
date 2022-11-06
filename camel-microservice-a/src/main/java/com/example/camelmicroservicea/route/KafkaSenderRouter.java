package com.example.camelmicroservicea.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class KafkaSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:json")
                .log("${body}")
                .to("kafka:myKafkaTopic");
    }
}
