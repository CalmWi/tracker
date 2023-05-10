package edu.grsu.tracker.api.rq.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRq {

    private String password;

    private String email;
}
