package com.jovisco.tutorial.webflux.aggregatorservice.trade;

public record TradeResponseDto(
    Integer customerId,
    Integer price,
    Integer quantity,
    TradeAction action,
    Integer totalPrice,
    Integer balance
) {
}
