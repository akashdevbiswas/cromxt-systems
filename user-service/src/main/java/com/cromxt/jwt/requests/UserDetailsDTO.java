package com.cromxt.jwt.requests;


import com.cromxt.jwt.entity.Gender;

public record UserDetailsDTO(
        String email,
        String password,
        String firstName,
        String lastName,
        Gender gender
) {
}
