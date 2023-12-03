package edu.grsu.tracker.rest.mapper;

import edu.grsu.tracker.api.rq.project.ProjectRq;
import edu.grsu.tracker.storage.entity.Project;
import edu.grsu.tracker.utils.CurrentUserHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Component
public class ProjectMapper {

    public Project toItem(final ProjectRq rq) {
        return Project.builder()
                .name(rq.getName())
                .type(rq.getType())
                .createOn(LocalDate.now())
                .issues(Collections.emptyList())
                .members(Set.of(Objects.requireNonNull(CurrentUserHolder.getCurrentUser())))
                .build();
    }
}
