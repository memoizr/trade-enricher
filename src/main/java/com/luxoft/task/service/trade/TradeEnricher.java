package com.luxoft.task.service.trade;

import com.luxoft.task.domain.EnrichedTrade;
import com.luxoft.task.domain.Product;
import com.luxoft.task.domain.Trade;
import com.luxoft.task.repository.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public final class TradeEnricher {

    private static final String MISSING_PRODUCT_NAME = "Missing Product Name";

    private final ProductRepository productDataSource;

    public TradeEnricher(ProductRepository productDataSource) {
        this.productDataSource = productDataSource;
    }

    public EnrichedTrade enrich(Trade trade) {
        final var productName = productDataSource
                .getById(trade.productId())
                .map(Product::productName)
                .orElse(MISSING_PRODUCT_NAME);

        return new EnrichedTrade(trade.date(), productName, trade.price());
    }
}
