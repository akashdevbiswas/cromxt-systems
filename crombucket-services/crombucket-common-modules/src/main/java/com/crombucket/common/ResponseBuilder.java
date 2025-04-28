package com.crombucket.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ResponseBuilder {

    <T> Mono<ResponseEntity<T>> buildResponseWithBody(Mono<T> responseMono, HttpStatus status);

    <T> Mono<ResponseEntity<T>> buildResponseWithBody(Mono<T> responseMono, HttpStatus status, String message);

    <T> Mono<ResponseEntity<T>> buildResponseWithBody(Mono<T> responseMono, HttpStatus status, String message, Map<String, String> headers);

    Mono<ResponseEntity<Void>> buildEmptyResponse(Mono<Void> emptyMono, HttpStatus status);

    Mono<ResponseEntity<Void>> buildEmptyResponse(Mono<Void> emptyMono, HttpStatus status, String message);

    Mono<ResponseEntity<Void>> buildEmptyResponse(Mono<Void> emptyMono, HttpStatus status, String message, Map<String, String> headers);


}
