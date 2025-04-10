package com.cromxt.crombucket.sdk.clients;

import com.cromxt.crombucket.sdk.FileVisibility;
import com.cromxt.crombucket.sdk.UpdateFileVisibilityRequest;
import com.cromxt.crombucket.sdk.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

abstract public class CromBucketWebClient extends CromBucketClient {

    abstract public FileResponse saveFile(MultipartFile file) throws IOException;

    abstract public FileResponse saveFile(MultipartFile file, FileVisibility fileVisibility) throws IOException;

    abstract public void deleteFile(String mediaId);

    abstract public void deleteMany(List<String> mediaIds);

    abstract public FileResponse changeFileVisibility(UpdateFileVisibilityRequest updateFileVisibilityRequest);

    abstract public List<FileResponse> changeFileVisibility(List<UpdateFileVisibilityRequest> updateFileVisibilityRequests);

    abstract public FileResponse updateFile(String mediaId, MultipartFile file) throws IOException;

    abstract public FileResponse updateFile(String mediaId, MultipartFile file, FileVisibility visibility) throws IOException;
}
