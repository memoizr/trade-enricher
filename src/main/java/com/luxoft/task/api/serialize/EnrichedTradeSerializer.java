package com.luxoft.task.api.serialize;

import com.luxoft.task.domain.EnrichedTrade;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;

@Service
public final class EnrichedTradeSerializer {

    private static final String HEADER = "date,product_name,currency,price";

    private final DecimalFormat decimalFormat = new DecimalFormat("0.0#");
    private final DateTimeFormatter dateFormatter = ofPattern("yyyyMMdd");

    public String serializeToCSV(Stream<EnrichedTrade> trades) {
        final var data = trades.map(this::serialize);

        return concat(Stream.of(HEADER), data)
                .collect(joining("\n"));
    }

    private String serialize(EnrichedTrade trade) {
        final var date = trade.date().format(dateFormatter);
        final var currencyCode = trade.price().getCurrency().getCurrencyCode();
        final var amount = decimalFormat.format(trade.price().getNumber().numberValue(BigDecimal.class));

        return date + "," + trade.productName() + "," + currencyCode + "," + amount;
    }
}
