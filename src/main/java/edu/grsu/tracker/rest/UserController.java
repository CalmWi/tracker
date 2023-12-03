package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.user.UserRq;
import edu.grsu.tracker.model.UserModel;
import edu.grsu.tracker.rest.mapper.UserMapper;
import edu.grsu.tracker.service.UserService;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserMapper mapper;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Operation(description = "Get all 'Users'.")
    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<List<UserModel>> getUsers() {
        List<UserModel> userModels = userService.getUsers().stream()
                .map(user -> modelMapper.map(user, UserModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userModels);
    }

    @Operation(description = "Get 'User' by id.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserModel> getUser(@Parameter(description = "User ID", required = true)
                                             @PathVariable("id") Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(modelMapper.map(user, UserModel.class));
    }

    @Operation(description = "Create a new 'User'.")
    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<UserModel> createUser(@RequestBody UserRq rq) {
        User created = userService.save(mapper.toItem(rq));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(created, UserModel.class));
    }

    @Operation(description = "Delete 'User' by id.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "User ID", required = true)
                                           @PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
