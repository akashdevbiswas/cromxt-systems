package com.cromxt.auth.requests;


import com.cromxt.auth.entity.Gender;

public record UserRequest(
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        Gender gender
) {
}
