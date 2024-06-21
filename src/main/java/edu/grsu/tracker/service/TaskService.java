package edu.grsu.tracker.service;

import edu.grsu.tracker.controller.exception.TrackerExceptoin;
import edu.grsu.tracker.storage.entity.Task;
import edu.grsu.tracker.storage.repo.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;

    public Task getTask(final Long id) {
        return taskRepo.findById(id).orElseThrow(() ->
                new TrackerExceptoin("Task not found")
        );
    }

    public List<Task> getTasks() {
        return taskRepo.findAll();
    }

    public List<Task> getLogTasks(final Integer month, final Integer year, final Long userId) {
        return taskRepo.findAllForMonthAndYear(month, year, userId);
    }

    public List<Task> getTasksByIssue(final Long issueId) {
        return taskRepo.findAllByIssueId(issueId);
    }

    public Task save(final Task task) {
        return taskRepo.save(task);
    }

    public Task update(final Long id, Task task) {
        Task get = getTask(id);
        get.setDate(task.getDate());
        get.setActivity(task.getActivity());
        get.setHours(task.getHours());
        get.setComment(task.getComment());
        return taskRepo.save(get);
    }

    public void delete(final Long id) {
        taskRepo.deleteById(id);
    }
}
