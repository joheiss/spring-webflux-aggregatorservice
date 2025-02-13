package com.jovisco.tutorial.webflux.aggregatorservice;

import com.jovisco.tutorial.webflux.aggregatorservice.trade.PriceUpdateResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class StockPriceStreamTest extends AbstractIntegrationTest {


    @Test
    public void priceStream() {
        // mock stock-service streaming response
        var responseBody = this.resourceToString("stock-service/stock-price-stream-200.jsonl");
        mockServerClient
                .when(HttpRequest.request("/stock/price-stream"))
                .respond(
                        HttpResponse.response(responseBody)
                                .withStatusCode(200)
                                .withContentType(MediaType.parse("application/x-ndjson"))
                );

        // we should get the streaming response via aggregator-service
        this.client.get()
                .uri("/stock/price-stream")
                .accept(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(PriceUpdateResponseDto.class)
                .getResponseBody()
                .doOnNext(price -> log.info("{}", price))
                .as(StepVerifier::create)
                .assertNext(p -> assertEquals(53, p.price()))
                .assertNext(p -> assertEquals(54, p.price()))
                .assertNext(p -> assertEquals(55, p.price()))
                .expectComplete()
                .verify();
    }

}
