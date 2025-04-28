package com.crombucket.storagemanager.exceptions;

public class MongoDBConnectionException extends RuntimeException{
    public MongoDBConnectionException(String message) {
        super(message);
    }
}
