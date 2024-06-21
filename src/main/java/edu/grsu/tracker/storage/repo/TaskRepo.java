package edu.grsu.tracker.storage.repo;

import edu.grsu.tracker.storage.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    @Query("select t from Task t join t.issue issue where issue.id = ?1 order by t.date desc")
    List<Task> findAllByIssueId(Long issueId);

    @Query("select t from Task t where month(t.date) = ?1 and year(t.date) = ?2 and t.userId = ?3 order by t.date desc")
    List<Task> findAllForMonthAndYear(Integer month, Integer year, Long userId);
}
