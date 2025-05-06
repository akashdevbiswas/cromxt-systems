package com.cromxt.auth.service;

import com.cromxt.auth.dtos.responses.UserResponse;
import com.cromxt.auth.entity.UserEntity;

public interface EntityMapperService {
    
    UserResponse getUserResponseFromUserEntity(UserEntity userEntity);
}
