package com.cromxt.mediamanager.service.impl;


import com.cromxt.common.crombucket.dtos.mediamanager.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediamanager.requests.UpdateMediaRequestDTO;
import com.cromxt.common.crombucket.dtos.mediamanager.response.MediaEntityDTO;
import com.cromxt.mediamanager.entity.Medias;
import com.cromxt.mediamanager.repository.MediaRepository;
import com.cromxt.mediamanager.service.MediaClientService;
import com.cromxt.mediamanager.service.MediaService;
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
    public Mono<MediaEntityDTO> createMedia(NewMediaRequest clientId) {
        Medias medias = Medias.builder()
                .build();
        return mediaRepository.save(medias).map(this::createMediaEntity);
    }

    @Override
    public Mono<MediaEntityDTO> updateMedia(String mediaId, UpdateMediaRequestDTO updateMediaRequestDTO){
//        TODO: This method is not implemented yet.
        return mediaRepository.findById(mediaId).flatMap(savedMedia->{
            savedMedia.setFileSize(updateMediaRequestDTO.fileSize());
            return mediaRepository.save(savedMedia).map(this::createMediaEntity);
        });

    }

    @Override
    public Mono<Void> deleteMediaById(String mediaId) {
        return mediaRepository.deleteById(mediaId);
    }

    private MediaEntityDTO createMediaEntity(Medias medias){
        return MediaEntityDTO.builder()
                .mediaId(medias.getMediaId())
                .fileId(medias.getFileId())
                .clientId(medias.getClientId())
                .fileSize(medias.getFileSize())
                .bucketId(medias.getBucketId())
                .extension(medias.getFileExtension())
                .build();
    }
}