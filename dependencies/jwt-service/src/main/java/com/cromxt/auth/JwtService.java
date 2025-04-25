package com.cromxt.auth;


import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;

public interface JwtService {

    UserDetails extractUserDetails(String token);

    String generateToken(UserDetails username);

    String generateSecret(String username) throws NoSuchAlgorithmException;

    boolean isTokenExpired(String token);
}
