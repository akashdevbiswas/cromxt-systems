package com.cromxt.routeservice.service;


import java.util.Comparator;

public class BucketComparator implements Comparator<BucketInfo> {
    @Override
    public int compare(BucketInfo o1, BucketInfo o2) {
        return o2.getAvailableSpaceInBytes().compareTo(o1.getAvailableSpaceInBytes());
    }
}