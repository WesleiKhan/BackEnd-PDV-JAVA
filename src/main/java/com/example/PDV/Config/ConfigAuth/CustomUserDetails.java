package com.example.PDV.Config.ConfigAuth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    private Integer id;

    private  String username;

    private  String password;

    private  String role;

    public CustomUserDetails() {

    }

    public CustomUserDetails(Integer id, String username, String password,
                             String role) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

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
