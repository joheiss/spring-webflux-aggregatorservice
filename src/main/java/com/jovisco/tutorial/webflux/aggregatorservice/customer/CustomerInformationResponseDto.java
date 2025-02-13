package com.jovisco.tutorial.webflux.aggregatorservice.customer;

import java.util.List;

public record CustomerInformationResponseDto(
        Integer id,
        String name,
        Integer balance,
        List<CustomerHoldingResponseDto> holdings
) {
}
