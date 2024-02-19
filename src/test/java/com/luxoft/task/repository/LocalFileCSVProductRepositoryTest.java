package com.luxoft.task.repository;

import com.luxoft.task.domain.Product;
import com.luxoft.task.service.localfile.LocalFileReader;
import com.luxoft.task.service.logger.AppLogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocalFileCSVProductRepositoryTest {

    final LocalFileReader dataFileService = mock(LocalFileReader.class);
    final AppLogger logger = mock(AppLogger.class);
    final LocalFileCSVProductRepository productRepository = new LocalFileCSVProductRepository(dataFileService, logger);

    @BeforeEach
    public void beforeEach() throws IOException {
        final var products = """
                    product_id, product_name
                    1,Treasury Bills Domestic
                    2,Corporate Bonds Domestic
                    3,REPO Domestic
                    4,Interest rate swaps International
                    5,OTC Index Option
                    6,Currency Options
                    7,Reverse Repos International
                    8,REPO International
                    9,766A_CORP BD
                    10,766B_CORP BD
                """;

        when(dataFileService.readFile()).thenReturn(new ByteArrayInputStream(products.getBytes()));

        productRepository.afterPropertiesSet();
    }

    @Test
    public void getsProductById() {
        assertEquals(Optional.of(new Product(6L, "Currency Options")), productRepository.getById(6L));
        assertEquals(Optional.of(new Product(10L, "766B_CORP BD")), productRepository.getById(10L));
    }

    @Test
    public void doesNotGetProductIfIdIsMissingAndLogsError() {
        assertEquals(empty(), productRepository.getById(42L));

        verify(logger).error("Could not find product with id `42`");
    }
}
