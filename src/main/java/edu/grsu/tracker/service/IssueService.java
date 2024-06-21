package edu.grsu.tracker.service;

import edu.grsu.tracker.controller.exception.TrackerExceptoin;
import edu.grsu.tracker.storage.entity.History;
import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.Task;
import edu.grsu.tracker.storage.repo.IssueRepo;
import edu.grsu.tracker.utils.CurrentUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepo issueRepo;
    private final TaskService taskService;
    private final HistoryService historyService;

    private final UserService userService;

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

    @Transactional
    public Issue save(final Issue issue) {
        issue.setLeftTime(issue.getEstimatedTime());
        issue.setSpentTime(0);
        History history = History.builder()
                .updateDate(LocalDateTime.now())
                .issue(issue)
                .user(CurrentUserHolder.getCurrentUser())
                .changes("Created")
                .build();
        historyService.save(history);

        return issueRepo.save(issue);
    }

    @Transactional
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
        get.setAssigned(issue.getAssigned());
        History history = History.builder()
                .updateDate(LocalDateTime.now())
                .issue(get)
                .user(CurrentUserHolder.getCurrentUser())
                .changes("Updated")
                .build();
        historyService.save(history);
        return issueRepo.save(get);
    }

    public void delete(final Long id) {
        Issue issue = getIssue(id);
        issue.getTasks().forEach(task -> taskService.delete(task.getId()));
        historyService.getIssueHistories(id).forEach(history -> historyService.delete(history.getId()));
        issueRepo.deleteById(id);
    }

    @Transactional
    public Issue addTaskToIssue(final Long issueId, final Long userId, final Task task) {
        Issue issue = getIssue(issueId);
        issue.getTasks().add(task);
        var spentTime = issue.getSpentTime() + task.getHours();
        var leftTime = issue.getEstimatedTime() - spentTime;

        issue.setSpentTime(spentTime);
        issue.setLeftTime(Math.max(leftTime, 0));

        task.setIssue(issue);
        var user = userService.getUser(userId);
        task.setUserId(userId);
        task.setUserFio(user.getFio());
        taskService.save(task);

        return issueRepo.save(issue);
    }

    @Transactional
    public Issue updateTaskFromIssue(final Long taskId, Task updatedTask) {
        var oldTask = taskService.getTask(taskId);
        var issue = oldTask.getIssue();
        var spentTime = issue.getSpentTime() + (updatedTask.getHours() - oldTask.getHours());
        var leftTime = issue.getEstimatedTime() - spentTime;
        issue.setSpentTime(spentTime);
        issue.setLeftTime(leftTime);

        taskService.update(taskId, updatedTask);
        return issueRepo.save(issue);
    }

    @Transactional
    public Issue deleteTaskFromIssue(final Long taskId) {
        var task = taskService.getTask(taskId);
        var issue = task.getIssue();
        var spentTime = issue.getSpentTime() - task.getHours();
        var leftTime = issue.getEstimatedTime() - spentTime;
        issue.setSpentTime(spentTime);
        issue.setLeftTime(leftTime);
        var savedIssue = issueRepo.save(issue);
        taskService.delete(taskId);
        return savedIssue;
    }
}
