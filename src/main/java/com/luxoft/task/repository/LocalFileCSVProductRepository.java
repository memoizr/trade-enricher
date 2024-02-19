package com.luxoft.task.repository;

import com.luxoft.task.domain.Product;
import com.luxoft.task.service.localfile.LocalFileReader;
import com.luxoft.task.service.localfile.LocalProductFileException;
import com.luxoft.task.service.logger.AppLogger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Long.parseLong;
import static java.util.Optional.empty;

@Service
public final class LocalFileCSVProductRepository implements ProductRepository, InitializingBean {

    private static final int PRODUCT_ID_INDEX = 0;
    private static final int PRODUCT_NAME_INDEX = 1;

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final LocalFileReader localFileReader;
    private final AppLogger logger;

    public LocalFileCSVProductRepository(
            LocalFileReader localFileReader,
            AppLogger logger
    ) {
        this.localFileReader = localFileReader;
        this.logger = logger;
    }

    @Override
    public void afterPropertiesSet() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(localFileReader.readFile()))) {
            reader.lines()
                    .skip(1) // Skip header
                    .map(line -> {
                        final var columns = line.trim().split(",");
                        final var productId = parseLong(columns[PRODUCT_ID_INDEX]);
                        final var productName = columns[PRODUCT_NAME_INDEX];
                        return new Product(productId, productName);
                    })
                    .forEach(product -> products.put(product.productId(), product));

        } catch (LocalProductFileException e) {
            logger.error("Could not initialize App properly, could not access products file at " + e.getFilePath());
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("Could not initialize App properly, invalid product file");
        } catch (IOException e) {
            logger.error("Could not initialize App properly, could not access products file");
        }
    }

    @Override
    public Optional<Product> getById(Long id) {
        final var product = products.get(id);

        if (product == null) {
            logger.error("Could not find product with id `" + id + "`");
            return empty();
        } else {
            return Optional.of(product);
        }
    }
}
