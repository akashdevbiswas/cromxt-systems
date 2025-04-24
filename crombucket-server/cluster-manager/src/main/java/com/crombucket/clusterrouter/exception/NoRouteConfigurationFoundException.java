package com.crombucket.clusterrouter.exception;

public class NoRouteConfigurationFoundException extends RuntimeException{
    public NoRouteConfigurationFoundException(String message) {
        super(message);
    }
}
