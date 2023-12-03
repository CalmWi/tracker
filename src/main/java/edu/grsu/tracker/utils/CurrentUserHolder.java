package edu.grsu.tracker.utils;

import edu.grsu.tracker.security.SecurityUser;
import edu.grsu.tracker.storage.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class CurrentUserHolder {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() instanceof String) {
            return null;
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        return securityUser.getUser();
    }
}
