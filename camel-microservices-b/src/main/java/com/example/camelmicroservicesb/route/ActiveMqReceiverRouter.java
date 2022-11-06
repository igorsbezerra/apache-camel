package com.example.camelmicroservicesb.route;

import com.example.camelmicroservicesb.exchange.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

//@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

    private final MyCurrencyExchangeProcessor myCurrentExchangeProcessor;
    private final MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;

    public ActiveMqReceiverRouter(MyCurrencyExchangeProcessor myCurrentExchangeProcessor, MyCurrencyExchangeTransformer myCurrencyExchangeTransformer) {
        this.myCurrentExchangeProcessor = myCurrentExchangeProcessor;
        this.myCurrencyExchangeTransformer = myCurrencyExchangeTransformer;
    }

    @Override
    public void configure() throws Exception {
        from("activemq:my-activemq-queue")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .bean(myCurrentExchangeProcessor)
                .bean(myCurrencyExchangeTransformer)
                .to("log:received-message-from-active-mq");
    }
}

@Component
class MyCurrencyExchangeProcessor {
    private final Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

    public void processMessage(CurrencyExchange currencyExchange) {
        logger.info("Do some processing with currencyExchange.getConversionMultiple() value which is {}",
                currencyExchange.getConversionMultiple());
    }
}

@Component
class MyCurrencyExchangeTransformer {
    public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {
        currencyExchange.setConversionMultiple(
                currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
        return currencyExchange;
    }
}
