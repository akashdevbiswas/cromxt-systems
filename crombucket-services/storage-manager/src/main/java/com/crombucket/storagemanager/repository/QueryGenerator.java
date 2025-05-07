package com.crombucket.storagemanager.repository;


import org.springframework.data.mongodb.core.query.Query;

public interface QueryGenerator {

    Query createQueryToFindAllStorageNodeWhereClusterCode(String clusterCode);

    Query createQueryToFindClustersByClustersCode(String clusterCode);

    Query createQueryToFindClusterHavingLargeSpace();

    Query createQueryToFindRegionByRegionCode(String regionCode);

    Query createQueryToFindAllRegionsByName(String regionName);
}
