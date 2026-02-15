package com.example.PDV.Config.ConfigAuth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties(value = {
        "enabled",
        "accountNonExpired",
        "accountNonLocked",
        "credentialsNonExpired"
})
public class CustomUserDetails implements UserDetails, Serializable {

    private final Integer id;

    private final String username;

    private final String password;

    private final List<GrantedAuthority> authorities = List.of();

    public CustomUserDetails(Integer id, String username, String password) {

        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Integer getId() {

        return id;
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
}
