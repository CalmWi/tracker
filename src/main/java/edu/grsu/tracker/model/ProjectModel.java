package edu.grsu.tracker.model;

import edu.grsu.tracker.storage.common.project.ProjectType;
import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectModel {
    private Long id;

    private String name;

    private LocalDate createOn;

    private ProjectType type;

    private Set<User> members;

    private List<Issue> issues;
}
