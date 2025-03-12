package com.example.web;


import com.cromxt.toolkit.crombucket.FileVisibility;
import com.cromxt.toolkit.crombucket.UpdateFileVisibilityRequest;
import com.cromxt.toolkit.crombucket.clients.CromBucketWebClient;
import com.cromxt.toolkit.crombucket.response.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/web")
public class WebController {

    private final CromBucketWebClient cromBucketWebClient;

    @PostMapping
    public FileResponse saveFile(@RequestParam("file") MultipartFile multipartFile,
                                 @RequestParam(value = "visibility", required = false,defaultValue = "PUBLIC" ) FileVisibility visibility
    ) throws IOException {
        return cromBucketWebClient.saveFile(multipartFile,visibility);
    }

    @DeleteMapping("/{mediaId}")
    public void deleteFile(@PathVariable String mediaId){
        cromBucketWebClient.deleteFile(mediaId);
    }

    @PatchMapping
    public FileResponse changeVisibility(@RequestBody UpdateFileVisibilityRequest updateFileVisibilityRequest){
        return cromBucketWebClient.changeFileVisibility(updateFileVisibilityRequest);
    }

}
