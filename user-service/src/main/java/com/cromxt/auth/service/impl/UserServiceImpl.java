package com.cromxt.auth.service.impl;


import com.cromxt.auth.JwtService;
import com.cromxt.auth.entity.Role;
import com.cromxt.auth.entity.UserEntity;
import com.cromxt.auth.repository.UserRepository;
import com.cromxt.auth.requests.UserCredentials;
import com.cromxt.auth.requests.UserDetailsDTO;
import com.cromxt.auth.responses.AuthTokens;
import com.cromxt.auth.service.AuthService;
import com.cromxt.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements AuthService, UserService {

    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Mono<AuthTokens> authenticateUserCredentials(UserCredentials userCredentials) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userCredentials.emailOrUsername(),userCredentials.password());
        return authenticationManager
                .authenticate(token)
                .map(authentication -> {
                    UserEntity principal = (UserEntity) authentication.getPrincipal();
                    String authToken = jwtService.generateToken(principal);
                    return new AuthTokens(authToken);
                });
    }

    @Override
    public Mono<Void> saveUser(UserDetailsDTO userDetailsDTO) {
        UserEntity userEntity = getUserEntity(userDetailsDTO);
        return userRepository.save(userEntity).then();
    }

    private UserEntity getUserEntity(UserDetailsDTO userDetailsDTO){
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
}
