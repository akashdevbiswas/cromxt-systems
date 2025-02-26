package com.cromxt.mediaserver.service.impl;


import com.cromxt.common.crombucket.dtos.mediaserver.requests.MediaStatus;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequest;
import com.cromxt.mediaserver.entity.Medias;
import com.cromxt.mediaserver.repository.MediaRepository;
import com.cromxt.mediaserver.service.MediaClientService;
import com.cromxt.mediaserver.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService, MediaClientService {

    private final MediaRepository mediaRepository;


    @Override
    public Mono<String> createMedia(NewMediaRequest mediaRequest) {
        return mediaRepository.save(Medias.builder()
                        .bucketId(mediaRequest.bucketId())
                        .fileExtension(mediaRequest.fileExtension())
                        .mediaName(mediaRequest.mediaId())
                        .userId(mediaRequest.userId())
                        .mediaStatus(mediaRequest.mediaStatus())
                        .build())
                .map(Medias::getId);
    }

    @Override
    public Mono<Void> updateMedia(String mediaId, UpdateMediaRequest mediaRequest){
        return mediaRepository.findById(mediaId).flatMap(savedMedia->{
            savedMedia.setSize(mediaRequest.size());
            savedMedia.setMediaStatus(MediaStatus.SUCCESS);
            return mediaRepository.save(savedMedia).then();
        });

    }

    @Override
    public Mono<Void> deleteMediaById(String mediaId) {
        return mediaRepository.deleteById(mediaId);
    }
}