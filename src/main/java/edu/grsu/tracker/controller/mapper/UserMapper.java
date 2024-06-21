package edu.grsu.tracker.controller.mapper;

import edu.grsu.tracker.dto.rq.auth.SignupRq;
import edu.grsu.tracker.dto.rq.user.UserRq;
import edu.grsu.tracker.storage.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toItem(final SignupRq rq) {
        return User.builder()
                .name(rq.getName())
                .surname(rq.getSurname())
                .fio(String.format("%s %s", rq.getName(), rq.getSurname()))
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
