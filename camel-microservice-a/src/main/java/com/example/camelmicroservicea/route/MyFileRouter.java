package com.example.camelmicroservicea.route;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyFileRouter extends RouteBuilder {

    private final DeciderBean deciderBean;

    public MyFileRouter(DeciderBean deciderBean) {
        this.deciderBean = deciderBean;
    }

    @Override
    public void configure() throws Exception {
        from("file:files/input")
                .routeId("Files-Input-Route")
                .transform().body(String.class)
                .choice()
                    .when(simple("${file:ext} == 'xml'"))
                        .log("XML FILE")
                    .when(method(deciderBean))
                        .log("NOT AN XML FILE BUT contains USD")
                    .otherwise()
                        .log("NOT AN XML FILE")
                .end()
//                .to("direct:log-file-values")
                .to("file:files/output");

        from("direct:log-file-values")
                .log("${routeId} ${camelId} ${body}");
    }
}

@Component
class DeciderBean {
    Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isThisConditionMet(
            @Body String body,
            @Headers Map<String, String> headers,
            @ExchangeProperties Map<String, String> exchangeProperties) {
        logger.info("{} {} {}", body, headers, exchangeProperties);
        return true;
    }
}
