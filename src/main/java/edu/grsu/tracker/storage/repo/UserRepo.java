package edu.grsu.tracker.storage.repo;

import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Query("select u from User u join u.projects project where project.id = ?1")
    List<User> findAllByProjectId(Long projectId);
}
