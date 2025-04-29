package com.crombucket.storagemanager.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder
@AllArgsConstructor
@Getter
public class Page<T> {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Long results;
    private Boolean isFirst;
    private Boolean isLast;
    private List<T> content;
}
