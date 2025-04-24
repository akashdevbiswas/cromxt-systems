package com.crombucket.mediaservice.controller;

import com.crombucket.mediaservice.service.MediaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/media")
public record MediaController(
        MediaService mediaService
) {


}
