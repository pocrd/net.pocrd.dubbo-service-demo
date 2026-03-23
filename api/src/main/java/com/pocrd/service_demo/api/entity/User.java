package com.pocrd.service_demo.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * User entity for API layer
 */
public record User(
    Long id,
    String username, 
    String email,
    String phone,
    Byte status,
    Date createdAt,
    Date updatedAt
) implements Serializable {
    private static final long serialVersionUID = 1L;
}