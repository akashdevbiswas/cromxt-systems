package com.crombucket.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
public class ResponseBuilderImpl implements ResponseBuilder {


    private static final HttpHeaders DEFAULT_SUCCESS_HEADERS = generateHeaders("Success",new HashMap<>());


    @Override
    public <T> Mono<ResponseEntity<T>> buildResponseWithBody(Mono<T> responseMono , HttpStatus status) {
        return buildResponseWithBody(responseMono,status,"Success",new HashMap<>());
    }

    @Override
    public <T> Mono<ResponseEntity<T>> buildResponseWithBody(Mono<T> responseMono, HttpStatus status, String message) {
        return buildResponseWithBody(responseMono,status,message,new HashMap<>());
    }

    @Override
    public <T> Mono<ResponseEntity<T>> buildResponseWithBody(Mono<T> responseMono, HttpStatus status, String message, Map<String, String> extraHeaders) {
        return responseMono.map(clusterResponse -> {
            HttpHeaders headers = generateHeaders(message, extraHeaders);
            return new ResponseEntity<>(clusterResponse,headers, status);
        }).onErrorResume(err->{
            log.error("Error occurred to perform operation {}",err.getMessage());
            HttpHeaders httpHeaders = generateHeaders(err.getMessage(),new HashMap<>());
            return Mono.just(ResponseEntity.badRequest().headers(httpHeaders).build());
        });
    }


    @Override
    public Mono<ResponseEntity<Void>> buildEmptyResponse(Mono<Void> emptyMono, HttpStatus status) {
        return buildEmptyResponse(emptyMono,status,"Success",new HashMap<>());
    }

    @Override
    public Mono<ResponseEntity<Void>> buildEmptyResponse(Mono<Void> emptyMono, HttpStatus status, String message) {
        return buildEmptyResponse(emptyMono,status,message,new HashMap<>());
    }

    @Override
    public Mono<ResponseEntity<Void>> buildEmptyResponse(Mono<Void> emptyMono, HttpStatus status, String message, Map<String, String> extraHeaders) {
        return emptyMono
                .then(Mono.just(new ResponseEntity<Void>(DEFAULT_SUCCESS_HEADERS,status)))
                .onErrorResume(err->{
                    HttpHeaders headers = generateHeaders(err.getMessage(), new HashMap<>());
                    return Mono.just(new ResponseEntity<>(headers, status));
                });
    }

    private static HttpHeaders generateHeaders(String message, Map<String, String> extraHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("message",message);
        if(Objects.isNull(extraHeaders) || extraHeaders.isEmpty()){
            return headers;
        }
        extraHeaders.forEach(headers::add);
        return headers;
    }
}
