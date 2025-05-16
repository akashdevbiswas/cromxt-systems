package com.crombucket.storagemanager.repository.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.crombucket.storagemanager.entity.Clusters;
import com.crombucket.storagemanager.entity.Regions;
import com.crombucket.storagemanager.repository.QueryGenerator;

@Service
public class QueryGeneratorImpl implements QueryGenerator {
    private static final String CLUSTER_CODE = "clusterCode";

    @Override
    public Query createQueryToFindAllStorageNodeWhereClusterCode(String clusterCode) {
        return new Query()
                .addCriteria(Criteria.where("clusters").is(Clusters.builder()
                        .clusterCode(clusterCode)
                        .build()));
    }

    @Override
    public Query createQueryToFindClustersByClustersCode(String clusterCode) {
        return new Query().addCriteria(Criteria.where(CLUSTER_CODE).is(clusterCode));
    }

    @Override
    public Query createQueryToFindClusterHavingLargeSpace() {
        Sort sort = Sort.by(Order.by("availableSpace").with(Sort.Direction.DESC));
        return new Query().with(sort);
    }

    @Override
    public Query createQueryToFindRegionByRegionCode(String regionCode) {
        return new Query().addCriteria(Criteria.where("regionCode").is(regionCode));
    }

    @Override
    public Query createQueryToFindAllRegionsByName(String regionNameOrCode) {
        Regions regionsExample = Regions.builder().regionName(regionNameOrCode).regionCode(regionNameOrCode).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        return new Query().addCriteria(new Criteria().alike(Example.of(regionsExample, matcher)));
    }

    @Override
    public Query createQueryToFindAllClustersByRegionNameOrCode(String regionCodeOrName) {
        return new Query().addCriteria(Criteria.where("regions")
                    .is(Regions.builder().regionCode(regionCodeOrName).build())
                    .orOperator(Criteria.where("regions").is(Regions.builder().regionName(regionCodeOrName).build())));
    }
}
