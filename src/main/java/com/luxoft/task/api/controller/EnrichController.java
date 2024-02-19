package com.luxoft.task.api.controller;

import com.luxoft.task.api.parse.TradeParser;
import com.luxoft.task.api.serialize.EnrichedTradeSerializer;
import com.luxoft.task.service.trade.TradeEnricher;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping("api/v1")
public final class EnrichController {

    private final TradeParser tradeParser;
    private final EnrichedTradeSerializer enrichedTradeSerializer;
    private final TradeEnricher tradeEnricher;

    public EnrichController(
            TradeParser tradeParser,
            EnrichedTradeSerializer enrichedTradeSerializer,
            TradeEnricher tradeEnricher
    ) {
        this.tradeParser = tradeParser;
        this.enrichedTradeSerializer = enrichedTradeSerializer;
        this.tradeEnricher = tradeEnricher;
    }

    @PostMapping(value = "enrich", consumes = "text/csv")
    public ResponseEntity<String> enrichTradeData(@RequestBody String tradeDataCSV) {
        final var data = tradeDataCSV.lines()
                .skip(1) // skip header
                .map(tradeParser::tryParse)
                .flatMap(Optional::stream) // filter only valid trades and get value
                .map(tradeEnricher::enrich);

        return ok()
                .contentType(new MediaType("text", "csv"))
                .header(CONTENT_DISPOSITION, "attachment; filename=\"enriched_trades.csv\"")
                .body(enrichedTradeSerializer.serializeToCSV(data));
    }
}
