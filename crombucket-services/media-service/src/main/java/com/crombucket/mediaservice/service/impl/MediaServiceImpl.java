package com.crombucket.mediaservice.service.impl;

import com.crombucket.common.MediaUploadStatus;
import com.crombucket.common.ObjectsVisibility;
import com.crombucket.common.mediaservice.requests.MediaObjectInitializeRequest;
import com.crombucket.common.mediaservice.response.MediaAddress;
import com.crombucket.common.mediaservice.response.MediaObjects;
import com.crombucket.mediaservice.clients.BucketServiceClient;
import com.crombucket.mediaservice.entity.Medias;
import com.crombucket.mediaservice.repository.MediaRepository;
import com.crombucket.mediaservice.service.EntityMapperService;
import com.crombucket.mediaservice.service.MediaService;
import com.crombucket.proto.storagenode.FileOperationResponse;
import com.crombucket.proto.storagenode.Visibility;

import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final BucketServiceClient bucketClient;
    private final EntityMapperService entityMapperService;

    @Override
    public Mono<MediaAddress> initializeMediaObject(MediaObjectInitializeRequest mediaRequest) {
        return bucketClient
                .fetchStorageNode(mediaRequest)
                .flatMap((res) -> {
                    String originalFileName = mediaRequest.originalfilename();

                    String fileExtension = getFileExtenstion(mediaRequest.originalfilename());
                    Medias newMedia = Medias.builder()
                            .bucketId(null)
                            .storageNodeId(res.storageId())
                            .clusterId(res.clusterId())
                            .fileExtension(fileExtension)
                            .preffredFileName(mediaRequest.prefferedFileName() == null
                                    ? getFileNameWithoutExtension(originalFileName)
                                    : mediaRequest.prefferedFileName())
                            .fileSize(mediaRequest.fileSize())
                            .visibility(mediaRequest.visibility())
                            .uploadStatus(MediaUploadStatus.PENDING)
                            .build();

                    return mediaRepository
                            .save(newMedia)
                            .map(savedMedia -> {
                                return new MediaAddress(res.url(), res.port(), savedMedia.getMediaId());
                            });
                })
                .onErrorResume(err -> {
                    // TODO: Handle the error and return a user readanble message.
                    return Mono.error(err);
                });
    }

    

    @Override
    public Mono<List<MediaObjects>> deleteMedias(List<String> mediaIds) {
        
        return null;
    }



    @Override
    public Mono<MediaObjects> getMediaObjectById(String mediaId) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public Mono<MediaObjects> updateVisibility(String mediaId, String visibility) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public Mono<MediaObjects> updateVisibility(List<String> mediaIds, String visibility) {
        // TODO Auto-generated method stub
        return null;
    }



    private String getFileExtenstion(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return fileName.substring(lastIndexOf);
    }

    private String getFileNameWithoutExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    private Mono<FileOperationResponse> changeFileVisibility(String fileId, Visibility visibility) {
        // ManagedChannel channel 
        return null;
    }   

}
