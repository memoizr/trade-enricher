package com.luxoft.task.repository;

import com.luxoft.task.domain.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> getById(Long id);
}
