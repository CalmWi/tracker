package edu.grsu.tracker.model;

import edu.grsu.tracker.storage.common.issue.IssuePriority;
import edu.grsu.tracker.storage.common.issue.IssueReason;
import edu.grsu.tracker.storage.common.issue.IssueStatus;
import edu.grsu.tracker.storage.common.issue.IssueType;
import edu.grsu.tracker.storage.entity.Project;
import edu.grsu.tracker.storage.entity.Task;
import edu.grsu.tracker.storage.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IssueModel {
    private Long id;

    private String name;

    private String description;

    private IssueType type;

    private IssueStatus status;

    private IssuePriority priority;

    private IssueReason reason;

    private User assigned;

    private Project project;

    private LocalDate startDate;

    private LocalDate dueDate;

    private LocalDate createOn;

    private Integer estimatedTime;

    private List<Task> tasks;
}
