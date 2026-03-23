package com.pocrd.service_demo.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Product entity for API layer
 */
public record Product(
    Long id,
    String productCode,
    String productName,
    BigDecimal price,
    Integer stock,
    String category
) implements Serializable {
    private static final long serialVersionUID = 1L;
}