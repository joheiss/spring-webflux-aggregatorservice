package com.jovisco.tutorial.webflux.aggregatorservice.customer;

import com.jovisco.tutorial.webflux.aggregatorservice.exception.ApplicationExceptionsFactory;
import com.jovisco.tutorial.webflux.aggregatorservice.exception.CustomerNotFoundException;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.TradeRequestDto;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.TradeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
public class CustomerServiceClient {

    private final WebClient client;

    public CustomerServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<CustomerInformationResponseDto> getCustomerInformation(Integer customerId) {
        return this.client.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerInformationResponseDto.class)
                .onErrorResume(NotFound.class, ex -> ApplicationExceptionsFactory.customerNotFound(customerId));
    }

    public Mono<TradeResponseDto> trade(Integer customerId, TradeRequestDto request) {
        return this.client.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TradeResponseDto.class)
                .onErrorResume(NotFound.class, ex -> ApplicationExceptionsFactory.customerNotFound(customerId))
                .onErrorResume(BadRequest.class, this::handleException);
    }

    private <T> Mono<T> handleException(BadRequest exception){
        var pd = exception.getResponseBodyAs(ProblemDetail.class);
        var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
        log.error("Customer service problem detail: {}", pd);
        return ApplicationExceptionsFactory.invalidTradeRequest(message);
    }
}