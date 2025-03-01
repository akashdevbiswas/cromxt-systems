package com.cromxt.bucket.controller;


import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Profile("local")
@RequestMapping("/bucket-client")
public class BucketManagerController {

    private final BucketService bucketService;


    @GetMapping
    public Mono<String> bucketClient(Model model) {
        model.addAttribute("message","Welcome to CROM-BUCKET local development server");
        return Mono.just("index");
    }

}
