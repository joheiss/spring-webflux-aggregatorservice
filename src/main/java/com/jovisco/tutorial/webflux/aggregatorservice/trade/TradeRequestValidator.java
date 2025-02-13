package com.jovisco.tutorial.webflux.aggregatorservice.trade;

import com.jovisco.tutorial.webflux.aggregatorservice.exception.ApplicationExceptionsFactory;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class TradeRequestValidator {

    public static UnaryOperator<Mono<TradeRequestDto>> validate() {
        return mono -> mono.filter(hasTicker())
                .switchIfEmpty(ApplicationExceptionsFactory.missingTicker())
                .filter(hasTradeAction())
                .switchIfEmpty(ApplicationExceptionsFactory.missingTradeAction())
                .filter(isValidQuantity())
                .switchIfEmpty(ApplicationExceptionsFactory.invalidQuantity());
    }

    private static Predicate<TradeRequestDto> hasTicker() {
        return dto -> Objects.nonNull(dto.ticker());
    }

    private static Predicate<TradeRequestDto> hasTradeAction() {
        return dto -> Objects.nonNull(dto.action());
    }

    private static Predicate<TradeRequestDto> isValidQuantity() {
        return dto -> Objects.nonNull(dto.quantity()) && dto.quantity() > 0;
    }

}
