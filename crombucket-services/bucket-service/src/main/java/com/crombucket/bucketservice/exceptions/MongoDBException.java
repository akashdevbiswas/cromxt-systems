package com.crombucket.bucketservice.exceptions;



public class MongoDBException extends RuntimeException {
    public MongoDBException(String message){
        super(message);
    }    
}
