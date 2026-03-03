package com.pocrd.service_demo.api;

import org.apache.dubbo.common.stream.StreamObserver;

/**
 * Greeter service interface definition
 * Using Dubbo Triple protocol
 */
public interface GreeterService {
    /**
     * Unary call - simple greeting
     * @param name the person's name
     * @return greeting message
     */
    String greet(String name);
    
    /**
     * Server streaming - greet multiple times
     * @param name the person's name
     * @param observer the stream observer to send multiple greetings
     */
    void greetStream(String name, StreamObserver<String> observer);
    
    /**
     * Bidirectional streaming - interactive greeting
     * @param observer the stream observer for bidirectional communication
     * @return the stream observer for sending responses
     */
    StreamObserver<String> greetInteractive(StreamObserver<String> observer);
}
