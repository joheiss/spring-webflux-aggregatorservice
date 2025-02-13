package com.jovisco.tutorial.webflux.aggregatorservice.trade;

public record StockPriceResponseDto(
        Ticker ticker,
        Integer price
) {
}
