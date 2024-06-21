package edu.grsu.tracker.storage.repo;

import edu.grsu.tracker.storage.entity.History;
import edu.grsu.tracker.storage.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepo extends JpaRepository<History, Long> {
    @Query("select h from History h join h.issue issue where issue.id = ?1 order by h.updateDate desc")
    List<History> findAllByIssueId(Long issueId);
}
