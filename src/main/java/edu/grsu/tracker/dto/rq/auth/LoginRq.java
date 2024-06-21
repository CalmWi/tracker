package edu.grsu.tracker.dto.rq.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRq {
    @NonNull
    @NotBlank
    private String password;
    @NonNull
    @NotBlank
    @Email
    private String email;
}
