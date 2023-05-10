package com.earthlyz9.stepin.auth;

import com.earthlyz9.stepin.entities.User;
import java.util.Collection;
import java.util.Collections;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Integer id;
    private String username;
    private String password;

    private User userObj;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Integer id, String username, String password,
        String role, User user) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        this.userObj = user;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User getUserObj() { return userObj; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 아래의 메소드는 사용하지 않을 것이므로 모두 true를 반환합니다.
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
