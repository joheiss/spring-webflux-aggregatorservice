package com.jovisco.tutorial.webflux.aggregatorservice.trade;

public record TradeRequestDto(
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction action
) {
    public Integer totalPrice() {
        return price * quantity;
    }
}
