package com.cromxt.auth.service.impl;

import org.springframework.stereotype.Service;

import com.cromxt.auth.dtos.responses.UserResponse;
import com.cromxt.auth.entity.UserEntity;
import com.cromxt.auth.service.EntityMapperService;


@Service
public class EntityMapperImpl implements EntityMapperService {

    @Override
    public UserResponse getUserResponseFromUserEntity(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getGender()
            );
    }
    

}
