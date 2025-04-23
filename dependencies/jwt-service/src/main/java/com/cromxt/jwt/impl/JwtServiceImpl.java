package com.cromxt.jwt.impl;

import com.cromxt.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final String secret;
    private final Long expiration;


    @Override
    public UserDetails extractUserDetails(String token) {
        return null;
    }

    @Override
    public String generateToken(String username) {
        return "";
    }

    @Override
    public String generateTokenWithExtraPayLoad(String username, Map<String, String> payload) {
        return "";
    }

    @Override
    public String generateSecret(String username) {
        return "";
    }
}
