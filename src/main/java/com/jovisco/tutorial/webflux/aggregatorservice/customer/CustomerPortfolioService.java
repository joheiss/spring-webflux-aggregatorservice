package com.jovisco.tutorial.webflux.aggregatorservice.customer;

import com.jovisco.tutorial.webflux.aggregatorservice.trade.StockPriceResponseDto;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.StockServiceClient;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.TradeRequestDto;
import com.jovisco.tutorial.webflux.aggregatorservice.trade.TradeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerPortfolioService {

    private final StockServiceClient stockServiceClient;
    private final CustomerServiceClient customerServiceClient;

    @Autowired
    public CustomerPortfolioService(StockServiceClient stockServiceClient, CustomerServiceClient customerServiceClient) {
        this.stockServiceClient = stockServiceClient;
        this.customerServiceClient = customerServiceClient;
    }

    public Mono<CustomerInformationResponseDto> getCustomerInformation(Integer customerId) {
        return customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<TradeResponseDto> trade(Integer customerId, TradeRequestDto request) {
        return this.stockServiceClient.getStockPrice(request.ticker())
                .map(StockPriceResponseDto::price)
                .map(price -> this.toStockTradeRequest(request, price))
                .flatMap(req -> this.customerServiceClient.trade(customerId, req));
    }

    private TradeRequestDto toStockTradeRequest(TradeRequestDto request, Integer price) {
        return new TradeRequestDto(
                request.ticker(),
                price,
                request.quantity(),
                request.action()
        );
    }
}
