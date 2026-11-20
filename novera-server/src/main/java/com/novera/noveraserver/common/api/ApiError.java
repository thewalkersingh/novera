package com.novera.noveraserver.common.api;

import java.time.Instant;
import java.util.Map;

/** Standard error envelope for every failed API call. */
public record ApiError(
        boolean success,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors,
        Instant timestamp) {
}