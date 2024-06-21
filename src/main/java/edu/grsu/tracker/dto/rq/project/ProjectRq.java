package edu.grsu.tracker.dto.rq.project;

import edu.grsu.tracker.storage.common.project.ProjectStatus;
import edu.grsu.tracker.storage.common.project.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRq {

    private String name;

    private ProjectType type;

    private ProjectStatus status;
}
