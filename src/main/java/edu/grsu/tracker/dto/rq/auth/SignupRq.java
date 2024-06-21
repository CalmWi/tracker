package edu.grsu.tracker.dto.rq.auth;

import edu.grsu.tracker.utils.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class SignupRq {
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @NotBlank
    private String surname;
    @NonNull
    @NotBlank
    private String password;
    @NonNull
    @NotBlank
    private String matchingPassword;
    @NonNull
    @NotBlank
    @Email
    private String email;
}
