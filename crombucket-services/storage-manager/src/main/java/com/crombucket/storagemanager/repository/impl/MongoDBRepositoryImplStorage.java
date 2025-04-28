package com.crombucket.storagemanager.repository.impl;

import com.crombucket.storagemanager.entity.StorageClusters;
import com.crombucket.storagemanager.entity.StorageNode;
import com.crombucket.storagemanager.exceptions.InvalidRequestException;
import com.crombucket.storagemanager.exceptions.MongoDBConnectionException;
import com.crombucket.storagemanager.repository.StorageClustersRepository;
import com.crombucket.storagemanager.repository.StorageNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDBRepositoryImplStorage implements StorageClustersRepository, StorageNodeRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    private static final String ID = "id";
    private static final String DEFAULT_ERROR_MESSAGE = "Some error occurred while performing a DB operation with message: ";


    @Override
    public Mono<StorageClusters> saveClusters(StorageClusters cluster) {
        return mongoTemplate.save(cluster).onErrorResume(err -> {
            log.error(err.getMessage());
            return Mono.error(new MongoDBConnectionException("An Unexpected error occurred while save the cluster."));
        });
    }

    @Override
    public Flux<StorageClusters> findAllClusters(Pageable pageable) {
        Query query = new Query()
                .with(pageable);
        return mongoTemplate.find(query, StorageClusters.class);
    }

    @Override
    public Mono<StorageClusters> findClusterById(String clusterId) {
        return mongoTemplate.findById(clusterId, StorageClusters.class);
    }

    @Override
    public Mono<Long> deleteCluster(String clusterId) {
        Query query = new Query().addCriteria(Criteria.where("clusterId").is(clusterId));

        return mongoTemplate.find(query, StorageNode.class)
                .count()
                .flatMap(storageClusters -> {
                    if (storageClusters > 0) {
                        throw new InvalidRequestException("The cluster is not empty, there are still active storage nodes.");
                    }
                    Query findClusterQuery = new Query().addCriteria(Criteria.where(ID).is(clusterId));
                    return mongoTemplate
                            .remove(findClusterQuery,StorageClusters.class)
                            .flatMap(deleteResult -> {
                                if (!deleteResult.wasAcknowledged()) {
                                    return Mono.error(new MongoDBConnectionException("The object is not deleted."));
                                }
                                return Mono.just(deleteResult.getDeletedCount());
                            });
                }).onErrorResume(err     -> {
                    if (err instanceof InvalidRequestException) {
                        log.error(err.getMessage());
                        return Mono.error(err);
                    } else if (err instanceof MongoDBConnectionException) {
                        log.error("Delete operation is un-successful.");
                        return Mono.error(err);
                    }
                    return Mono.error(new MongoDBConnectionException(DEFAULT_ERROR_MESSAGE + "Unsuccessful delete operation."));
                });
    }

}
