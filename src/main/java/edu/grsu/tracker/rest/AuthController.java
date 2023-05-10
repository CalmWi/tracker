package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.auth.LoginRq;
import edu.grsu.tracker.api.rq.auth.SignupRq;
import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.rest.mapper.UserMapper;
import edu.grsu.tracker.service.AuthService;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper mapper;

    @Operation(description = "Login to the app and get token.")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRq rq) {
        String token = authService.authenticateUser(rq.getEmail(), rq.getPassword());

        return ResponseEntity.ok().body(token);
    }

    @Operation(description = "Register a new User.")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRq signUpRq) {
        if (!signUpRq.getPassword().equals(signUpRq.getMatchingPassword())
                || !signUpRq.getEmail().contains("@")) {
            throw new TrackerExceptoin("Incorrect data! Please check input info.");
        }
        User registered = authService.registerUser(mapper.toItem(signUpRq));
        return ResponseEntity.ok(registered);
    }

    @Operation(description = "Sign out from app.")
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("You've been signed out!");
    }
}
