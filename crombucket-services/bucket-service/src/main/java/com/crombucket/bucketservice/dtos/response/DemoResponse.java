package com.crombucket.bucketservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import reactor.core.publisher.Flux;


@AllArgsConstructor
@Builder
public class DemoResponse {

    private String name;
    private Flux<String> hobby;
}
