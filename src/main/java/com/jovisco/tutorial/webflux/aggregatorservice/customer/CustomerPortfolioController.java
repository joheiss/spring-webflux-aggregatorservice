package com.jovisco.tutorial.webflux.aggregatorservice.customer;

import com.jovisco.tutorial.webflux.aggregatorservice.trade.TradeRequestDto;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.TradeRequestValidator;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.TradeResponseDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformationResponseDto> getCustomerInformation(@PathVariable Integer customerId) {
        return this.customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<TradeResponseDto> trade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequestDto> mono) {
        return mono.transform(TradeRequestValidator.validate())
                .flatMap(req -> this.customerPortfolioService.trade(customerId, req));
    }

}
