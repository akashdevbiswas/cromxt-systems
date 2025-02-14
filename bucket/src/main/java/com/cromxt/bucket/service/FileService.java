package com.cromxt.bucket.service;


import com.cromxt.proto.files.MediaHeaders;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;

public interface FileService {
    FileDetails generateFileDetails(MediaHeaders contentType);
    String getFileAbsolutePath(String fileName);

}

