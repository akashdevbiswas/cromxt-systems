package com.crombucket.storagemanager.repository.impl;

import com.crombucket.storagemanager.entity.StorageClusters;
import com.crombucket.storagemanager.repository.QueryGenerator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class QueryGeneratorImpl implements QueryGenerator {
    private static final String CLUSTER_CODE = "clusterCode";

    @Override
    public Query createQueryToFindAllStorageNodeWhereClusterCode(String clusterCode){
        return new Query()
                .addCriteria(Criteria.where("clusters").is(StorageClusters.builder()
                        .clusterCode(clusterCode)
                        .build()
                ));
    }
    @Override
    public Query createQueryToFindClustersByClustersCode(String clusterCode){
        return new Query().addCriteria(Criteria.where(CLUSTER_CODE).is(clusterCode));
    }
}
