package com.pocrd.service_demo.api.entity;

import java.io.Serializable;

/**
 * User entity for API layer
 */
public record User(
    Long id,
    String username, 
    String email,
    String phone
) implements Serializable {
    private static final long serialVersionUID = 1L;
}