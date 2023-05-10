package edu.grsu.tracker.storage.common.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum UserRole {
    Admin(Set.of(Permission.Edit, Permission.Write, Permission.Member)),
    Manager(Set.of(Permission.Edit,Permission.Member)),
    User(Set.of(Permission.Member));
    private Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
