package com.cromxt.auth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public enum Role {

    ADMIN(Set.of(
            "admin:read",
            "admin:write"
    )),
    USER(Set.of(
            "user:read",
            "user:write"
    ));

    private final Set<String> authorities;

    Role(Set<String> authorities){
        this.authorities = authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        String role = this.name();
        grantedAuthorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s",role)));
        this.authorities.forEach(authority->{
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        });
        return grantedAuthorities;
    }
}
