package com.crombucket.mediaservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crombucket.common.mediaservice.requests.MediaObjectInitializeRequest;
import com.crombucket.common.mediaservice.response.MediaAddress;
import com.crombucket.common.mediaservice.response.MediaObjects;
import com.crombucket.mediaservice.dtos.MediaObjectResponse;
import com.crombucket.mediaservice.dtos.requests.MediaUploadRequest;
import com.crombucket.mediaservice.service.MediaService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/medias")
@RequiredArgsConstructor
public class MediaController {
        private final MediaService mediaService;

        @PostMapping
        public Mono<ResponseEntity<MediaAddress>> saveMedia(@RequestBody MediaObjectInitializeRequest mediaSaveRequest) {
                return null;
        }

        @PostMapping(value = "/upload")
        public Mono<ResponseEntity<MediaObjectResponse>> uploadMedia(@ModelAttribute MediaUploadRequest mediaUploadRequest) {
                return null;
        }


        @GetMapping(value = "/{mediaId}")
        public Mono<ResponseEntity<MediaObjects>> getMediaObjectById(@PathVariable String mediaId) {
                // return mediaService.getMediaObjectById(mediaId);
                return null;
        }

        public Mono<ResponseEntity<List<MediaObjects>>> deleteMediaObjects(@RequestParam List<String> mediaIds) {
                return null;
        }



}
