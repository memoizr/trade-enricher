package com.luxoft.task.integration;

import com.luxoft.task.api.parse.TradeParser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TradeParser.class)
public class EnrichControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void transformsProductsCsv() {
        final var tradesCSV = """
            date,product_id,currency,price
            20160101,1,EUR,10.0
            20160101,2,EUR,20.1
            20160101,3,EUR,30.34
            20160101,11,EUR,35.34
            """;

        final var expectedCSV = """
            date,product_name,currency,price
            20160101,Treasury Bills Domestic,EUR,10.0
            20160101,Corporate Bonds Domestic,EUR,20.1
            20160101,REPO Domestic,EUR,30.34
            20160101,Missing Product Name,EUR,35.34
            """;

        webTestClient.post().uri("api/v1/enrich")
                .contentType(new MediaType("text", "csv"))
                .bodyValue(tradesCSV)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    final var responseBody = response.getResponseBody();

                    assertEquals(expectedCSV.trim(), responseBody.trim());
                });
    }
}
