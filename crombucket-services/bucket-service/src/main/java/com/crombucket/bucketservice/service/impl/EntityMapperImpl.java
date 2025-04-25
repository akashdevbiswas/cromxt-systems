package com.crombucket.bucketservice.service.impl;

import com.crombucket.bucketservice.dtos.request.BucketRequest;
import com.crombucket.bucketservice.enity.BucketGroups;
import com.crombucket.bucketservice.enity.Buckets;
import com.crombucket.bucketservice.service.EntityMapper;
import org.springframework.stereotype.Service;


@Service
public class EntityMapperImpl implements EntityMapper {


    public Buckets generateBucketFromBucketRequest(BucketRequest bucketRequest){
        return null;
    }
    public BucketGroups generateBucketGroup(String bucketName){
        return null;
    }

}
