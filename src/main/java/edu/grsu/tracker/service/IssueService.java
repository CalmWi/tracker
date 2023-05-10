package edu.grsu.tracker.service;

import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.Task;
import edu.grsu.tracker.storage.repo.IssueRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepo issueRepo;
    private final TaskService taskService;

    public Issue getIssue(final Long id) {
        return issueRepo.findById(id).orElseThrow(() ->
                new TrackerExceptoin("Issue not found")
        );
    }

    public List<Issue> getIssues() {
        return issueRepo.findAll();
    }

    public List<Issue> getAssignedIssues(final Long userId) {
        return issueRepo.findAllByUserId(userId);
    }

    public Issue save(final Issue issue) {
        return issueRepo.save(issue);
    }

    public Issue update(final Long id, Issue issue) {
        Issue get = getIssue(id);
        get.setName(issue.getName());
        get.setDescription(issue.getDescription());
        get.setDueDate(issue.getDueDate());
        get.setEstimatedTime(issue.getEstimatedTime());
        get.setReason(issue.getReason());
        get.setPriority(issue.getPriority());
        get.setStartDate(issue.getStartDate());
        get.setStatus(issue.getStatus());
        get.setType(issue.getType());
        return issueRepo.save(get);
    }

    public void delete(final Long id) {
        Issue issue = getIssue(id);
        issue.getTasks().forEach(task -> taskService.delete(task.getId()));
        issueRepo.deleteById(id);
    }

    @Transactional
    public Issue addTaskToIssue(final Long issueId, final Task task) {
        Issue issue = getIssue(issueId);
        issue.getTasks().add(task);

        task.setIssue(issue);
        taskService.save(task);

        return issueRepo.save(issue);
    }
}
