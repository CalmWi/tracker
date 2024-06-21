package edu.grsu.tracker.service;

import edu.grsu.tracker.controller.exception.TrackerExceptoin;
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

    public List<User> getProjectUsers(final Long projectId) {
        return userRepo.findAllByProjectId(projectId);
    }

    public User save(final User user) {
        return userRepo.save(user);
    }
    public User update(final Long id, User user) {
        User get = getUser(id);
        get.setName(user.getName());
        get.setSurname(user.getSurname());
        get.setFio(String.format("%s %s", user.getName(), user.getSurname()));
        get.setEmail(user.getEmail());
        get.setRole(user.getRole());
        return userRepo.save(get);
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
