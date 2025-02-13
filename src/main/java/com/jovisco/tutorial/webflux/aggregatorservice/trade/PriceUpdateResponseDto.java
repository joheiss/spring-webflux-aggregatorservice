package com.jovisco.tutorial.webflux.aggregatorservice.trade;

import java.time.LocalDateTime;

public record PriceUpdateResponseDto(
        Ticker ticker,
        Integer price,
        LocalDateTime time
) {
}
