package com.novera.noveraserver.common.api;

import org.springframework.data.domain.Page;

import java.util.List;

/** Stable page shape for list endpoints (we do not serialize Spring's PageImpl directly). */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}