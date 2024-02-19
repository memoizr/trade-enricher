package com.luxoft.task.parse;

import com.luxoft.task.api.parse.TradeParser;
import com.luxoft.task.domain.Trade;
import com.luxoft.task.service.logger.AppLogger;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDate.parse;
import static java.util.Optional.empty;
import static javax.money.Monetary.getCurrency;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TradeParserTest {

    final AppLogger mockAppLogger = mock(AppLogger.class);
    final TradeParser parser = new TradeParser(mockAppLogger);

    @Test
    public void parsesTrade() {
        final var trades = """
                20160101,1,EUR,10.0
                20160101,2,USD,20.1
                20160101,3,GBP,30.34
                20160101,11,EUR,35.3
                xxx,1,EUR,10.0
                20160101,lala,EUR,10.0
                20160101,1,PESOS,10.0
                20160101,1,EUR,lala
                20160101,1,EUR
                """;

        final var expectedTrades = List.of(
                Optional.of(new Trade(parse("2016-01-01"), 1L, Money.of(10.0, getCurrency("EUR")))),
                Optional.of(new Trade(parse("2016-01-01"), 2L, Money.of(20.1, getCurrency("USD")))),
                Optional.of(new Trade(parse("2016-01-01"), 3L, Money.of(30.34, getCurrency("GBP")))),
                Optional.of(new Trade(parse("2016-01-01"), 11L, Money.of(35.3, getCurrency("EUR")))),
                empty(),
                empty(),
                empty(),
                empty(),
                empty()
        );

        final var parsedTrades = trades.lines()
                .map(parser::tryParse)
                .collect(Collectors.toList());

        assertEquals(expectedTrades, parsedTrades);
    }

    @Test
    public void logsDateErrors() {
        parser.tryParse("xxx,1,EUR,10.0");

        verify(mockAppLogger).error("Invalid date (expecting format yyyyMMdd) at column 0 in `xxx,1,EUR,10.0`");
    }

    @Test
    public void logsProductIdErrors() {
        parser.tryParse("20160101,lala,EUR,10.0");

        verify(mockAppLogger).error("Invalid productId at column 1 or money amount at column 3 in `20160101,lala,EUR,10.0`");
    }

    @Test
    public void logsCurrencyErrors() {
        parser.tryParse("20160101,1,PESITO,10.0");

        verify(mockAppLogger).error("Invalid currency at column 2 in `20160101,1,PESITO,10.0`");
    }

    @Test
    public void logsMoneyAmountIdErrors() {
        parser.tryParse("20160101,1,EUR,lala");

        verify(mockAppLogger).error("Invalid productId at column 1 or money amount at column 3 in `20160101,1,EUR,lala`");
    }

    @Test
    public void logsInsufficientColumnsErrors() {
        parser.tryParse("20160101,1,EUR");

        verify(mockAppLogger).error("Invalid number of columns in CSV, expecting 4 columns");
    }
}
