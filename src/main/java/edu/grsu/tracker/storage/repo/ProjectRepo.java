package edu.grsu.tracker.storage.repo;

import edu.grsu.tracker.storage.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    @Query("select p from Project p join p.members m where m.id = ?1")
    List<Project>  findAllByMember(Long userId);
}
