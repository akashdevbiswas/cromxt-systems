package com.cromxt.mediaservice.controller;

import com.cromxt.mediaservice.service.MediaService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/media")
public record MediaController(
        MediaService mediaService
) {


}
