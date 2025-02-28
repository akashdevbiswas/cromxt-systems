package com.cromxt.bucket.service;


import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.proto.files.MediaDetails;
import com.cromxt.proto.files.MediaHeaders;
import reactor.core.publisher.Flux;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;

public interface FileService {
    FileDetails generateFileDetails(String contentType);
    String getFileAbsolutePath(String mediaId);

    Flux<MediaObjects> getAllAvailableMedias();
}

