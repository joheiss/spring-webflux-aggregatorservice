package com.jovisco.tutorial.webflux.aggregatorservice.customer;

import com.jovisco.tutorial.webflux.aggregatorservice.trade.Ticker;

public record CustomerHoldingResponseDto(
        Ticker ticker,
        Integer quantity
) {
}
