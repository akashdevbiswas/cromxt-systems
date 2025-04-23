package com.cromxt.jwt;


import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    UserDetails extractUserDetails(String token);

    String generateToken(String username);

    String generateTokenWithExtraPayLoad(String username, Map<String,String> payload);

    String generateSecret(String username);
}
