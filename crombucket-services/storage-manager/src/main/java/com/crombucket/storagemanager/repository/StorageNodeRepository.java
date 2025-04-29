package com.crombucket.storagemanager.repository;

import com.crombucket.storagemanager.entity.StorageNode;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface StorageNodeRepository {

    Mono<StorageNode> save(StorageNode newStorageNode);

    Mono<Page<StorageNode>> findAllStorageNodesByClusterCode(String clusterCode, Pageable pageable);

}
