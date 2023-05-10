package edu.grsu.tracker.service;

import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.storage.entity.User;
import edu.grsu.tracker.storage.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public User getUser(final Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new TrackerExceptoin("User not found")
        );
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User save(final User user) {
        return userRepo.save(user);
    }

    public void delete(final Long id) {
        userRepo.deleteById(id);
    }

    public User getUserByEmail(final String email) {
        return userRepo.findByEmail(email).orElseThrow(() ->
                new TrackerExceptoin("User not found")
        );
    }

    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }
}
