package com.cromxt.bucket.controller;


import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class BucketManagerController {

    private final BucketService bucketService;

    @GetMapping
    public Flux<MediaObjects> getAllMediaObjects(){
        return bucketService.getAllAvailableMedias();
    }


    @GetMapping(value = "/{mediaId}")
    public Mono<MediaObjects> getMediaObject(@PathVariable String mediaId){
        return bucketService.getMediaObject(mediaId);
    }
}
