package edu.grsu.tracker.storage.repo;

import edu.grsu.tracker.storage.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepo extends JpaRepository<Issue, Long> {
    @Query("select i from Issue i join i.assigned user where user.id = ?1 order by i.createOn desc")
    List<Issue> findAllByUserId(Long userId);
}
