package com.luxoft.task.domain;

import org.javamoney.moneta.Money;

import java.time.LocalDate;

public record EnrichedTrade(
        LocalDate date,
        String productName,
        Money price) {

}
