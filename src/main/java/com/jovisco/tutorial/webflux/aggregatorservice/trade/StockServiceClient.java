package com.jovisco.tutorial.webflux.aggregatorservice.trade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@Slf4j
public class StockServiceClient {

    private final WebClient client;
    private Flux<PriceUpdateResponseDto> flux;

    public StockServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<StockPriceResponseDto> getStockPrice(Ticker ticker) {
        return this.client.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponseDto.class);
    }

    public Flux<PriceUpdateResponseDto> priceUpdatesStream() {
        if (Objects.isNull(this.flux)) {
            this.flux = this.getPriceUpdates();
        }
        return this.flux;
    }

    private Flux<PriceUpdateResponseDto> getPriceUpdates() {
        return this.client.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdateResponseDto.class)
                .retryWhen(retry())
                .cache(1);
    }

    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(
                        rs -> log.error("stock service price stream call failed. retrying: {}",
                        rs.failure().getMessage())
                );
    }
}
