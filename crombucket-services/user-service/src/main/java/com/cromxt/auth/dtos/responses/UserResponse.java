package com.cromxt.auth.dtos.responses;

import com.cromxt.auth.entity.Gender;

public record UserResponse(
    String username,
    String email,
    String firstName,
    String lastName,
    Gender gender
) {

}
