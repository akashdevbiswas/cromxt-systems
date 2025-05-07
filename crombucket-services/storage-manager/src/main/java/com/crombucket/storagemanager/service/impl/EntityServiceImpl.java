package com.crombucket.storagemanager.service.impl;

import com.crombucket.storagemanager.dtos.requests.BucketRequest;
import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.requests.RegionRequest;
import com.crombucket.storagemanager.dtos.requests.StorageNodeRequest;
import com.crombucket.storagemanager.dtos.response.BucketResponse;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.RegionResponse;
import com.crombucket.storagemanager.dtos.response.StorageNodeResponse;
import com.crombucket.storagemanager.entity.Clusters;
import com.crombucket.storagemanager.entity.Regions;
import com.crombucket.storagemanager.entity.StorageNode;
import com.crombucket.storagemanager.exceptions.InvalidRequestException;
import com.crombucket.storagemanager.exceptions.MongoDBConnectionException;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.repository.RegionRepository;
import com.crombucket.storagemanager.repository.ClustersRepository;
import com.crombucket.storagemanager.repository.StorageNodeRepository;
import com.crombucket.storagemanager.service.BucketService;
import com.crombucket.storagemanager.service.EntityMapperService;
import com.crombucket.storagemanager.service.RegionService;
import com.crombucket.storagemanager.service.StorageClusterService;
import com.crombucket.storagemanager.service.StorageNodeService;
import com.crombucket.storagemanager.utility.ClusterSortingOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntityServiceImpl implements StorageClusterService, StorageNodeService, RegionService, BucketService {

    private static final String DEFAULT_ERROR_MESSAGE = "Some error occurred while performing a DB operation with message: ";

    private final EntityMapperService entityMapperService;
    private final ClustersRepository storageClustersRepository;
    private final StorageNodeRepository storageNodeRepository;
    private final RegionRepository regionRepository;

    @Override
    public Mono<ClusterResponse> createNewCluster(ClusterRequest clusterRequest) {
        Clusters storageCluster = entityMapperService.createClusterEntityFromClusterRequest(clusterRequest);
        return storageClustersRepository.saveClusters(storageCluster)
                .map(entityMapperService::createStorageClustersResponseFromStorageCluster);
    }

    @Override
    public Mono<Page<ClusterResponse>> getAllStorageClusters(Integer pageNumber, Integer pageSize,
            ClusterSortingOrder order) {

        final Sort sort = ClusterSortingOrder.getSortingOrder(order);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return storageClustersRepository
                .findAllClusters(pageable)
                .map(storageClustersPage -> {
                    List<ClusterResponse> storageClusterResponseList = storageClustersPage.getContent().stream()
                            .map(entityMapperService::createStorageClustersResponseFromStorageCluster).toList();
                    return entityMapperService.pageResponseBuilder(storageClusterResponseList, storageClustersPage);
                });
    }

    @Override
    public Mono<Long> deleteCluster(String clusterCode) {
        return storageClustersRepository
                .deleteCluster(clusterCode)
                .onErrorResume(err -> {
                    if (err instanceof InvalidRequestException) {
                        log.error(err.getMessage());
                        return Mono.error(err);
                    } else if (err instanceof MongoDBConnectionException) {
                        log.error("Delete operation is un-successful.");
                        return Mono.error(err);
                    }
                    return Mono.error(
                            new MongoDBConnectionException(DEFAULT_ERROR_MESSAGE + "Unsuccessful delete operation."));
                });
    }

    @Override
    public Mono<ClusterResponse> getClusterById(String clusterCode) {
        return storageClustersRepository.findClusterByClusterCode(clusterCode)
                .map(entityMapperService::createStorageClustersResponseFromStorageCluster);
    }

    @Override
    public Mono<StorageNodeResponse> createStorageNode(StorageNodeRequest nodeRequest) {
        StorageNode newStorageNode = entityMapperService.createStorageNodeFromNodeRequest(nodeRequest);
        return storageNodeRepository.save(newStorageNode)
                .map(entityMapperService::createStorageResponseFromStorageNodeEntity);
    }

    @Override
    public Mono<Page<StorageNodeResponse>> getAllStorageNodesByClusterCode(
            String clusterCode,
            Integer pageNumber,
            Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return storageNodeRepository
                .findAllStorageNodesByClusterCode(clusterCode, pageable)
                .map(storageNodePage -> {
                    List<StorageNode> content = storageNodePage.getContent();
                    List<StorageNodeResponse> storageNodeResponseList = content.stream()
                            .map(entityMapperService::createStorageResponseFromStorageNodeEntity).toList();
                    return entityMapperService.pageResponseBuilder(storageNodeResponseList, storageNodePage);
                });
    }

    @Override
    public Mono<Long> deleteNode(String nodeCode) {
        return storageNodeRepository.deleteStorageNode(nodeCode);
    }

    @Override
    public Mono<BucketResponse> createBucket(BucketRequest bucketRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBucket'");
    }

    @Override
    public Mono<RegionResponse> createRegion(RegionRequest regionRequest) {
        Regions region = Regions.builder()
                .regionName(regionRequest.regionName())
                .regionCode(regionRequest.regionCode())
                .build();
        return regionRepository.saveRegion(region)
                .map(entityMapperService::createRegionResponseFromRegions);
    }

    @Override
    public Mono<RegionResponse> getRegionByCode(String regionCode) {
        return regionRepository.findRegionByRegionCode(regionCode)
                .map(entityMapperService::createRegionResponseFromRegions);
    }

    @Override
    public Mono<Page<RegionResponse>> findAllRegionsByName(String regionName, Integer pageNumber, Integer pageSize) {
        return regionRepository
                .findAllRegionsByName(regionName,
                        PageRequest.of(pageNumber, pageSize, Sort.by(Order.desc("startedOn"))))
                .map(regionPage -> {
                    List<RegionResponse> regionResponseList = regionPage.getContent().stream()
                            .map(entityMapperService::createRegionResponseFromRegions).toList();
                    return entityMapperService.pageResponseBuilder(regionResponseList, regionPage);
                });
    }

}
