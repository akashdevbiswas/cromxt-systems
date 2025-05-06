package com.cromxt.auth.service.impl;


import java.util.HashMap;
import java.util.List;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cromxt.auth.dtos.responses.UserResponse;
import com.cromxt.auth.entity.Role;
import com.cromxt.auth.entity.UserEntity;
import com.cromxt.auth.repository.UserRepository;
import com.cromxt.auth.requests.UserCredentials;
import com.cromxt.auth.requests.UserRequest;
import com.cromxt.auth.responses.AuthTokens;
import com.cromxt.auth.service.AuthService;
import com.cromxt.auth.service.EntityMapperService;
import com.cromxt.auth.service.UserService;
import com.cromxt.authentication.JwtService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements AuthService, UserService {

    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapperService entityMapperService;


    @Override
    public Mono<AuthTokens> authenticateUserCredentials(UserCredentials userCredentials) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userCredentials.emailOrUsername(),userCredentials.password());
        return authenticationManager
                .authenticate(token)
                .map(authentication -> {
                    UserEntity user = (UserEntity) authentication.getPrincipal();
                    List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                    String authToken = jwtService.generateToken(user.getId(), authorities ,new HashMap<>());
                    return new AuthTokens(authToken);
                });
    }

    @Override
    public Mono<Void> saveUser(UserRequest userDetailsDTO) {
        UserEntity userEntity = getUserEntity(userDetailsDTO);
        return userRepository.save(userEntity).then();
    }

    private UserEntity getUserEntity(UserRequest userDetailsDTO){
        return UserEntity.builder()
                .username(userDetailsDTO.username())
                .email(userDetailsDTO.email())
                .role(Role.USER)
                .firstName(userDetailsDTO.firstName())
                .lastName(userDetailsDTO.lastName())
                .password(passwordEncoder.encode(userDetailsDTO.password()))
                .gender(userDetailsDTO.gender())
                .build();
    }

    @Override
    public Mono<UserResponse> getUserById(String userId) {
        return userRepository.findById(userId).map(entityMapperService::getUserResponseFromUserEntity);
    }
}
