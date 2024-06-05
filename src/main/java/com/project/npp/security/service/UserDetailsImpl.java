package com.project.npp.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.npp.entities.Operator;
import com.project.npp.entities.UserEntity;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;


    @JsonIgnore
    private String password;

    private GrantedAuthority authority;
 
    private Operator operator;

    public UserDetailsImpl(Integer id, String username, String password,
            GrantedAuthority authority, Operator operator) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.operator=operator;
    }

    public static UserDetailsImpl build(UserEntity user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().name());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getPasswordHash(),
                authority,
                user.getOperator());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority); // Returns a list with a single authority
    }

    public Integer getId() {
        return id;
    }
    public Operator getOperator() {
        return operator;
    }

  

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
