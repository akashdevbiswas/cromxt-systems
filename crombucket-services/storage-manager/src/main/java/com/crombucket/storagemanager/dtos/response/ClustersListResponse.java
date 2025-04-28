package com.crombucket.storagemanager.dtos.response;

import java.util.List;

public record ClustersListResponse (
        Integer pageNumber,
        Integer results,
        Boolean isLast,
        List<ClusterResponse> clusters
){
}
