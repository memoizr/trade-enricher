package com.luxoft.task.domain;

import org.javamoney.moneta.Money;

import java.time.LocalDate;

public record Trade(
        LocalDate date,
        Long productId,
        Money price
) {

}
