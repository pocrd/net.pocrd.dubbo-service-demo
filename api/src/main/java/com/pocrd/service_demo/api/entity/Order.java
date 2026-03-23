package com.pocrd.service_demo.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Order entity for API layer
 */
public record Order(
    Long id,
    String orderNo,
    Long userId,
    BigDecimal amount,
    Byte status,
    String remark
) implements Serializable {
    private static final long serialVersionUID = 1L;
}