package edu.grsu.tracker.rest.mapper;

import edu.grsu.tracker.api.rq.auth.SignupRq;
import edu.grsu.tracker.api.rq.user.UserRq;
import edu.grsu.tracker.storage.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toItem(final SignupRq rq) {
        return User.builder()
                .name(rq.getName())
                .surname(rq.getSurname())
                .password(new BCryptPasswordEncoder().encode(rq.getPassword()))
                .email(rq.getEmail())
                .build();
    }

    public User toItem(final UserRq rq) {
        return User.builder()
                .name(rq.getName())
                .surname(rq.getSurname())
                .password(new BCryptPasswordEncoder().encode(rq.getPassword()))
                .email(rq.getEmail())
                .role(rq.getUserRole())
                .build();
    }
}
