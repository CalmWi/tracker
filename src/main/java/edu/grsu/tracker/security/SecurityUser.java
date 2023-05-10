package edu.grsu.tracker.security;

import edu.grsu.tracker.storage.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Data
public class SecurityUser implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String name;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public SecurityUser(String username, String password,
                        Collection<? extends GrantedAuthority> authorities) {
        this.name = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
        SecurityUser user = (SecurityUser) o;
        return Objects.equals(name, user.name);
    }

    public static SecurityUser fromUser(User user) {
        return new SecurityUser(
                user.getEmail(), user.getPassword(), user.getRole().getAuthorities());
    }
}
