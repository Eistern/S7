package com.contacts.demo.security.data.types;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@RequiredArgsConstructor
public class UserEntry implements UserDetails {

    @NotEmpty
    private final String username;

    @NotEmpty
    @Setter private String password;

    @NotEmpty
    @Setter private List<String> userAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> resultedAuthorities = new ArrayList<>();
        userAuthorities.forEach((role) -> resultedAuthorities.add(new SimpleGrantedAuthority(role)));
        return resultedAuthorities;
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
