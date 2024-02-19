package com.luxoft.task.api.parse;

import com.luxoft.task.domain.Trade;
import com.luxoft.task.service.logger.AppLogger;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.money.MonetaryException;

import static java.lang.Long.parseLong;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.empty;
import static javax.money.Monetary.getCurrency;

@Service
public final class TradeParser {

    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final int DATE_INDEX = 0;
    private static final int PRODUCT_ID_INDEX = 1;
    private static final int CURRENCY_INDEX = 2;
    private static final int AMOUNT_INDEX = 3;
    private static final int EXPECTED_COLUMN_COUNT = 4;

    private final AppLogger logger;

    public TradeParser(AppLogger logger) {
        this.logger = logger;
    }

    private final DateTimeFormatter dateTimeFormatter = ofPattern(DATE_PATTERN);

    public Optional<Trade> tryParse(String trade) {
        var columns = trade.split(",");

        try {
            var date = parse(columns[DATE_INDEX], dateTimeFormatter);
            var productId = parseLong(columns[PRODUCT_ID_INDEX]);
            var currency = getCurrency(columns[CURRENCY_INDEX]);
            var amount = new BigDecimal(columns[AMOUNT_INDEX]);

            return Optional.of(new Trade(date, productId, Money.of(amount, currency)));
        } catch (DateTimeParseException e) {
            logger.error(
                    "Invalid date (expecting format " + DATE_PATTERN + ") at column " + DATE_INDEX + " in `" + trade
                            + "`");
        } catch (MonetaryException e) {
            logger.error("Invalid currency at column " + CURRENCY_INDEX + " in `" + trade + "`");
        } catch (NumberFormatException e) {
            logger.error(
                    "Invalid productId at column " + PRODUCT_ID_INDEX + " or money amount at column " + AMOUNT_INDEX
                            + " in `" + trade + "`");
        } catch (IndexOutOfBoundsException e) {
            logger.error("Invalid number of columns in CSV, expecting " + EXPECTED_COLUMN_COUNT + " columns");
        }

        return empty();
    }
}
