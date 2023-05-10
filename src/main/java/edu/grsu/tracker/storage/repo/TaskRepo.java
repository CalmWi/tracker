package edu.grsu.tracker.storage.repo;

import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {
}
