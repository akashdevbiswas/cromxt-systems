package com.example.web;


import com.cromxt.toolkit.crombucket.FileVisibility;
import com.cromxt.toolkit.crombucket.clients.CromBucketWebClient;
import com.cromxt.toolkit.crombucket.response.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/web")
public class WebController {

    private final CromBucketWebClient cromBucketWebClient;

    @PostMapping
    public FileUploadResponse saveFile(@RequestParam("file") MultipartFile multipartFile,
                                       @RequestParam(value = "visibility", required = false,defaultValue = "PUBLIC" ) FileVisibility visibility
    ) throws IOException {
        return cromBucketWebClient.saveFile(multipartFile,visibility);
    }

}
