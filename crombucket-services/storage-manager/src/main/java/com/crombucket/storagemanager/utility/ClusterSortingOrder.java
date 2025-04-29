package com.crombucket.storagemanager.utility;

import org.springframework.data.domain.Sort;

public enum ClusterSortingOrder {
    NEWEST,
    OLDER,
    MAX_STORAGE_AVAILABLE,
    MINIMUM_STORAGE_AVAILABLE;

    public static Sort getSortingOrder(ClusterSortingOrder order){
        return switch (order) {
            case NEWEST -> Sort.by(Sort.Order.by("createdOn"));
            case OLDER -> Sort.by(Sort.Order.asc("createdOn"));
            case MAX_STORAGE_AVAILABLE -> Sort.by(Sort.Order.by("capacity"));
            case MINIMUM_STORAGE_AVAILABLE -> Sort.by(Sort.Order.asc("capacity"));
        };
    }
}
