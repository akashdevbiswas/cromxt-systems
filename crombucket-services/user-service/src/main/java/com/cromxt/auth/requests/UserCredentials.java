package com.cromxt.auth.requests;



public record UserCredentials (
        String emailOrUsername,
        String password
){
}
