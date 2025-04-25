package com.cromxt.auth.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/demo")
//@PreAuthorize(value = "hasRole('USER')")
public class DemoController {

    @GetMapping
    public Mono<String> demo(){
        return Mono.just("Hello this is a authenticated endpoint.");
    }
}
