package edu.grsu.tracker.security;

import edu.grsu.tracker.service.IssueService;
import edu.grsu.tracker.service.ProjectService;
import edu.grsu.tracker.service.TaskService;
import edu.grsu.tracker.storage.common.project.ProjectType;
import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.Project;
import edu.grsu.tracker.storage.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PermissionCheck {
    private final IssueService issueService;
    private final TaskService taskService;
    private final ProjectService projectService;

    public boolean checkTaskDate(final Long id) {
        Task task = taskService.getTask(id);
        return !task.getCreateOn().plusDays(7).isBefore(LocalDate.now());
    }

    public boolean checkIssueDate(final Long id) {
        Issue issue = issueService.getIssue(id);
        return !issue.getCreateOn().plusDays(7).isBefore(LocalDate.now());
    }

    public boolean checkTypeProject(final Long id) {
        Project project = projectService.getProject(id);
        return project.getType().equals(ProjectType.PTO);
    }
}
