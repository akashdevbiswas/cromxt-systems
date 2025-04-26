package com.crombucket.bucketservice.service.impl;

import com.crombucket.bucketservice.dtos.request.StorageRequest;
import com.crombucket.bucketservice.dtos.request.BucketRequest;
import com.crombucket.bucketservice.enity.BucketGroups;
import com.crombucket.bucketservice.enity.Buckets;
import com.crombucket.bucketservice.service.EntityMapper;
import org.springframework.stereotype.Service;


@Service
public class EntityMapperImpl implements EntityMapper {

    @Override
    public Buckets generateBucketFromBucketRequest(BucketRequest bucketRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BucketGroups generateBucketsGroupFromBucketGroupRequest(StorageRequest bucketGroupRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
