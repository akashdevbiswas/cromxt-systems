package com.cromxt.crombucket.dtos.requests;


import com.cromxt.crombucket.entity.Gender;

public record UserDetailsDTO(
        String email,
        String password,
        String firstName,
        String lastName,
        Gender gender
) {
}
