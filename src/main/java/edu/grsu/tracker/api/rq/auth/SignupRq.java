package edu.grsu.tracker.api.rq.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRq {

    private String name;

    private String surname;

    private String password;

    private String matchingPassword;

    private String email;
}
