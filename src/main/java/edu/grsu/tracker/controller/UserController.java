package edu.grsu.tracker.controller;

import edu.grsu.tracker.dto.rq.user.UserRq;
import edu.grsu.tracker.dto.model.UserModel;
import edu.grsu.tracker.controller.mapper.UserMapper;
import edu.grsu.tracker.service.UserService;
import edu.grsu.tracker.storage.common.user.UserRole;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Users")
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserMapper mapper;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Operation(description = "Get all 'Users'.")
    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('write')")
    public String getUsers(Model model) {
        List<UserModel> userModels = userService.getUsers().stream()
                .map(user -> modelMapper.map(user, UserModel.class))
                .collect(Collectors.toList());
        model.addAttribute("users", userModels);
        return "users";
    }

    @Operation(description = "Get current 'User'")
    @GetMapping(value = "/account")
    public String getCurrentUser(@RequestAttribute("userId") Long userId, Model model) {
        User user = userService.getUser(userId);
        model.addAttribute("user", modelMapper.map(user, UserModel.class));
        return "account";
    }

    @Operation(description = "Get 'User' by id.")
    @GetMapping(value = "/{id}")
    public String getUser(@Parameter(description = "User ID", required = true)
                          @PathVariable("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", modelMapper.map(user, UserModel.class));
        return "account";
    }

    @Operation(description = "Get form new 'User'.")
    @GetMapping(value = "/add")
    @PreAuthorize("hasAuthority('write')")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserRq());
        model.addAttribute("roles", UserRole.values());
        return "create-user";
    }

    @Operation(description = "Create a new 'User'.")
    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('write')")
    public ModelAndView createUser(UserRq rq) {
        User created = userService.save(mapper.toItem(rq));
        return new ModelAndView("account", "user", modelMapper.map(created, UserModel.class));
    }

    @Operation(description = "Get form for update 'User'.")
    @GetMapping(value = "/edit/{id}")
    @PreAuthorize("hasAuthority('write')")
    public String showEditUserForm(@Parameter(description = "User ID", required = true)
                                   @PathVariable("id") Long id, Model model) {
        model.addAttribute("user", modelMapper.map(userService.getUser(id), UserModel.class));
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("userId", id);
        return "update-user";
    }

    @Operation(description = "Update a 'User'.")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ModelAndView updateUser(@Parameter(description = "User ID", required = true)
                                   @PathVariable("id") Long id, UserRq rq) {
        User updated = userService.update(id, mapper.toItem(rq));
        return new ModelAndView("account", "user", modelMapper.map(updated, UserModel.class));
    }

    @Operation(description = "Delete 'User' by id.")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('write')")
    public String deleteUser(@Parameter(description = "User ID", required = true)
                             @PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
