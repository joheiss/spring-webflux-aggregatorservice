package com.jovisco.tutorial.webflux.aggregatorservice.config;

import com.jovisco.tutorial.webflux.aggregatorservice.customer.CustomerServiceClient;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.StockServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ServiceClientsConfig {

    @Bean
    public CustomerServiceClient customerServiceClient(@Value("${customer.service.url}") String baseUrl) {
        return new CustomerServiceClient(createWebClient(baseUrl));
    }

    @Bean
    public StockServiceClient stockServiceClient(@Value("${stock.service.url}") String baseUrl) {
        return new StockServiceClient(createWebClient(baseUrl));
    }

    private WebClient createWebClient(String baseUrl) {
        log.info("base url: {}", baseUrl);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
