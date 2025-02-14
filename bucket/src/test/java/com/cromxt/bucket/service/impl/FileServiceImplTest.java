package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.MediaHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class FileServiceImplTest {

    @Autowired
    private FileService fileService;



    @Test
    void shouldCreateAFileWithTheDetailsWithMediaObjectMetaData() {
        MediaHeaders mediaMetaData = MediaHeaders.newBuilder()
                .setHlsStatus(true)
                .setContentType("mp4")
                .build();
        FileDetails fileDetails = fileService.generateFileDetails(mediaMetaData);

        assertEquals(mediaMetaData.getContentType(),fileDetails.getContentType());
        assertNotNull(fileDetails);
        assertNotNull(fileDetails.getAbsolutePath());
        assertNotNull(fileDetails.getFileId());
    }
}