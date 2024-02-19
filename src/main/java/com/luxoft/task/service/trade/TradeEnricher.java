package com.luxoft.task.service.trade;

import com.luxoft.task.domain.EnrichedTrade;
import com.luxoft.task.domain.Product;
import com.luxoft.task.domain.Trade;
import com.luxoft.task.repository.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public final class TradeEnricher {

    private static final String MISSING_PRODUCT_NAME = "Missing Product Name";

    private final ProductRepository productRepository;

    public TradeEnricher(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public EnrichedTrade enrich(Trade trade) {
        final var productName = productRepository
                .getById(trade.productId())
                .map(Product::productName)
                .orElse(MISSING_PRODUCT_NAME);

        return new EnrichedTrade(trade.date(), productName, trade.price());
    }
}
