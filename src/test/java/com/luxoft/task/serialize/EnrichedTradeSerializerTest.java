package com.luxoft.task.serialize;

import com.luxoft.task.api.serialize.EnrichedTradeSerializer;
import com.luxoft.task.domain.EnrichedTrade;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrichedTradeSerializerTest {

    private final EnrichedTradeSerializer serializer = new EnrichedTradeSerializer();

    @Test
    public void serializesTradesToCSV() {
        final var tradesCSV = serializer.serializeToCSV(Stream.of(
                new EnrichedTrade(parse("2021-01-01"), "hello", Money.of(5.1, "USD")),
                new EnrichedTrade(parse("2021-01-02"), "world", Money.of(6.64, "USD"))
        ));

        final var expectedCSV = """
                date,product_name,currency,price
                20210101,hello,USD,5.1
                20210102,world,USD,6.64
                """;

        assertEquals(expectedCSV.trim(), tradesCSV);
    }
}
