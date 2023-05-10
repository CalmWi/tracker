package edu.grsu.tracker.service;

import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.security.SecurityUser;
import edu.grsu.tracker.security.jwt.JwtTokenProvider;
import edu.grsu.tracker.storage.common.user.UserRole;
import edu.grsu.tracker.storage.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public User registerUser(final User user) {

        if (userService.emailExists(user.getEmail())) {
            throw new TrackerExceptoin("There is an account with that email address: "
                    + user.getEmail());
        }
        user.setRole(UserRole.User);
        return userService.save(user);
    }

    public String authenticateUser(final String email, final String password) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();

        return jwtTokenProvider.generateJwtToken(userPrincipal);
    }
}
