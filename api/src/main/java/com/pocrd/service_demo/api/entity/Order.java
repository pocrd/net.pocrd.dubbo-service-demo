package com.pocrd.service_demo.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Order entity for API layer
 */
public record Order(
    Long id,
    String orderNo,
    Long userId,
    BigDecimal amount,
    Byte status,
    String remark,
    Date createdAt,
    Date updatedAt
) implements Serializable {
    private static final long serialVersionUID = 1L;
}