package com.crombucket.common;

import org.springframework.http.HttpHeaders;

import java.util.Map;

public interface UtilService {
    HttpHeaders generateHeaders(String message,Map<String, String> extraHeaders);
}
