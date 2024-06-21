package edu.grsu.tracker.controller.mapper;

import edu.grsu.tracker.dto.rq.project.ProjectRq;
import edu.grsu.tracker.storage.entity.Project;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
public class ProjectMapper {

    public Project toItem(final ProjectRq rq) {
        return Project.builder()
                .name(rq.getName())
                .type(rq.getType())
                .status(rq.getStatus())
                .createOn(LocalDate.now())
                .issues(Collections.emptyList())
                .members(Collections.emptySet())
                .build();
    }
}
