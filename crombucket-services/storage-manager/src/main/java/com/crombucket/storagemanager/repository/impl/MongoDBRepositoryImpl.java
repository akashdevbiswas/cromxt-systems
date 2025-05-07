package com.crombucket.storagemanager.repository.impl;

import com.crombucket.storagemanager.entity.Clusters;
import com.crombucket.storagemanager.entity.Regions;
import com.crombucket.storagemanager.entity.StorageNode;
import com.crombucket.storagemanager.exceptions.InvalidRequestException;
import com.crombucket.storagemanager.exceptions.MongoDBConnectionException;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.repository.QueryGenerator;
import com.crombucket.storagemanager.repository.RegionRepository;
import com.crombucket.storagemanager.repository.BucketRepository;
import com.crombucket.storagemanager.repository.ClustersRepository;
import com.crombucket.storagemanager.repository.StorageNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import javax.swing.plaf.synth.Region;


@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDBRepositoryImpl implements ClustersRepository, StorageNodeRepository,BucketRepository, RegionRepository {

    private final ReactiveMongoTemplate mongoTemplate;
    private final QueryGenerator queryGenerator;

    private static final String ID = "id";


    @Override
    public Mono<Clusters> saveClusters(Clusters cluster) {
        return mongoTemplate.save(cluster).onErrorResume(err -> {
            log.error(err.getMessage());
            return Mono.error(new MongoDBConnectionException("An Unexpected error occurred while save the cluster."));
        });
    }

    @Override
    public Mono<Page<Clusters>> findAllClusters(Pageable pageable) {
        Query query = new Query();
        return findAllPageable(Clusters.class,query,pageable);
    }

    @Override
    public Mono<Clusters> findClusterByClusterCode(String clusterCode) {
        Query query = queryGenerator.createQueryToFindClustersByClustersCode(clusterCode);
        return mongoTemplate.findOne(query, Clusters.class);
    }

    @Override
    public Mono<Long> deleteCluster(String clusterCode) {
        Query query = queryGenerator.createQueryToFindAllStorageNodeWhereClusterCode(clusterCode);
        return mongoTemplate.count(query, StorageNode.class)
                .flatMap(storageClusters -> {
                    if (storageClusters > 0) {
                        throw new InvalidRequestException("The cluster is not empty, there are still active storage nodes.");
                    }
                    Query findClusterQuery = new Query().addCriteria(Criteria.where(ID).is(clusterCode));
                    return mongoTemplate
                            .remove(findClusterQuery, Clusters.class)
                            .flatMap(deleteResult -> {
                                if (!deleteResult.wasAcknowledged()) {
                                    return Mono.error(new MongoDBConnectionException("The object is not deleted."));
                                }
                                return Mono.just(deleteResult.getDeletedCount());
                            });
                });
    }


    @Override
    public Mono<StorageNode> save(StorageNode newStorageNode) {
        return mongoTemplate.save(newStorageNode);
    }

    @Override
    public Mono<Page<StorageNode>> findAllStorageNodesByClusterCode(String clusterCode, Pageable pageable) {
        Query findClustersByClustersCode = queryGenerator.createQueryToFindAllStorageNodeWhereClusterCode(clusterCode);
        return findAllPageable(StorageNode.class,findClustersByClustersCode,pageable);
    }

    @Override
    public Mono<Long> deleteStorageNode(String nodeCode) {
        return null;
    }

    private <T> Mono<Page<T>> findAllPageable(Class<T> entity, Query query, Pageable pageable) {
        return mongoTemplate.count(query, entity)
                .flatMap(storageNodeCount -> {
                    Pageable modifiedPage = validatePageable(storageNodeCount, pageable);
                    return mongoTemplate
                            .find(query.with(modifiedPage), entity)
                            .collectList()
                            .map(storageNode -> generatePage(storageNode, modifiedPage, storageNodeCount));
                });
    }

    private <T> Page<T> generatePage(List<T> results, Pageable pageable, Long totalResults) {
        int totalPageNumber = (int) Math.ceil((double) totalResults / pageable.getPageSize());
        return Page.<T>builder()
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .results(totalResults)
                .totalPages(totalPageNumber)
                .isLast(totalPageNumber == (pageable.getPageNumber() + 1))
                .isFirst(pageable.getPageNumber() == 0)
                .content(results)
                .build();
    }

    private Pageable validatePageable(Long totalNumberOfResults, Pageable pageable) {
        int totalNumberOfPages = (int) Math.ceil((double) totalNumberOfResults / pageable.getPageSize());
        if(pageable.getPageNumber() ==0){
            return pageable;
        }
        int pageNumber = totalNumberOfPages < (pageable.getPageNumber() * pageable.getPageSize()) ? totalNumberOfPages - 1 : pageable.getPageNumber();
        return PageRequest.of(pageNumber,pageable.getPageSize(),pageable.getSort());
    }

    @Override
    public Mono<Clusters> getLargeCluster() {
        Query findLargeSpaceAvailableCluster = queryGenerator.createQueryToFindClusterHavingLargeSpace();
        return mongoTemplate.findOne(findLargeSpaceAvailableCluster, Clusters.class);
    }

    @Override
    public Mono<Regions> saveRegion(Regions region) {
        return mongoTemplate.save(region);
    }

    @Override
    public Mono<Regions> findRegionByRegionCode(String regionCode) {
        Query query = queryGenerator.createQueryToFindRegionByRegionCode(regionCode);
        return mongoTemplate.findOne(query, Regions.class);
    }

    @Override
    public Mono<Page<Regions>> findAllRegionsByName(String regionName, Pageable pageable) {
        Query query = queryGenerator.createQueryToFindAllRegionsByName(regionName);
        return findAllPageable(Regions.class, query, pageable);
    }
    

}
