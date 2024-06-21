package edu.grsu.tracker.controller;

import edu.grsu.tracker.dto.rq.auth.LoginRq;
import edu.grsu.tracker.dto.rq.auth.SignupRq;
import edu.grsu.tracker.controller.exception.TrackerExceptoin;
import edu.grsu.tracker.controller.mapper.UserMapper;
import edu.grsu.tracker.service.AuthService;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Tag(name = "Auth")
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper mapper;

    @GetMapping("/login")
    public String loginUser(Model model) {
        model.addAttribute("user", new SignupRq());
        return "login";
    }

    @Operation(description = "Login to the app and get token.")
    @PostMapping("/signin")
    public String authenticateUser(@ModelAttribute("user") @Valid LoginRq rq, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            String token = authService.authenticateUser(rq.getEmail(), rq.getPassword());
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(1 * 24 * 60 * 60);
            response.addCookie(cookie);
        } catch (AuthenticationException e) {
            throw new TrackerExceptoin("Invalid email/password combination");
        }
        return "redirect:/home";
    }

    @Operation(description = "Get registration page.")
    @GetMapping("/signup")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new SignupRq());
        return "register";
    }

    @Operation(description = "Register a new User.")
    @PostMapping("/signup")
    public String registerUser(@Valid SignupRq signUpRq, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        User registered = authService.registerUser(mapper.toItem(signUpRq));
        return "redirect:/login?success";
    }

    @Operation(description = "Sign out from app.")
    @PostMapping("/signout")
    public String logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/auth/login";
    }
}
