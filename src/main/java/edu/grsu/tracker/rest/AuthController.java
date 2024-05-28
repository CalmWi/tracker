package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.auth.LoginRq;
import edu.grsu.tracker.api.rq.auth.SignupRq;
import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.rest.mapper.UserMapper;
import edu.grsu.tracker.service.AuthService;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    public ModelAndView authenticateUser(@ModelAttribute("user") @Valid @NotNull LoginRq rq, HttpServletResponse response) {
        try {
        String token = authService.authenticateUser(rq.getEmail(), rq.getPassword());
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(1 * 24 * 60 * 60);
        response.addCookie(cookie);
        } catch (AuthenticationException e) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", "Invalid email/password combination");
            return mav;
        }
        return new ModelAndView("redirect:/home", "user", rq);
    }
    @Operation(description = "Get registration page.")
    @GetMapping("/signup")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new SignupRq());
        return "register";
    }
    @Operation(description = "Register a new User.")
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") @Valid SignupRq signUpRq) {
        if (!signUpRq.getPassword().equals(signUpRq.getMatchingPassword())
                || !signUpRq.getEmail().contains("@")) {
            throw new TrackerExceptoin("Incorrect data! Please check input info.");
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
