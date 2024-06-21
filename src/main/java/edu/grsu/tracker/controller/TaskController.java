package edu.grsu.tracker.controller;

import edu.grsu.tracker.dto.rq.task.TaskRq;
import edu.grsu.tracker.dto.model.TaskModel;
import edu.grsu.tracker.controller.mapper.TaskMapper;
import edu.grsu.tracker.service.TaskService;
import edu.grsu.tracker.storage.common.task.TaskActivity;
import edu.grsu.tracker.storage.entity.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Tasks")
@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final ModelMapper mapper;

    @Operation(description = "Get all 'Tasks'.")
    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<List<TaskModel>> getTasks() {
        List<TaskModel> taskList = taskService.getTasks().stream()
                .map(task -> mapper.map(task, TaskModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskList);
    }

    @Operation(description = "Get all 'Tasks'.")
    @GetMapping(value = "/report")
    @PreAuthorize("hasAuthority('member')")
    public String getLogTasks(@RequestAttribute("userId") Long userId,
                              @RequestParam(value = "month", required = false) Integer month,
                              @RequestParam(value = "year", required = false) Integer year, Model model) {
        List<TaskModel> taskList = taskService.getLogTasks(month, year, userId).stream()
                .map(task -> mapper.map(task, TaskModel.class))
                .collect(Collectors.toList());
        model.addAttribute("tasks", taskList);
        model.addAttribute("curMonth", month);
        model.addAttribute("curYear", year);
        return "report";
    }

    @Operation(description = "Get 'Task' by id.")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<TaskModel> getTask(@Parameter(description = "Task ID", required = true)
                                             @PathVariable("id") Long id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok(mapper.map(task, TaskModel.class));
    }

    @Operation(description = "Get all 'Task' by issue id.")
    @GetMapping(value = "/issue/{issueId}")
    @PreAuthorize("hasAuthority('member')")
    public String getIssueTasks(@PathVariable("issueId") Long issueId, Model model) {
        List<TaskModel> tasks = taskService.getTasksByIssue(issueId).stream()
                .map(task -> mapper.map(task, TaskModel.class))
                .collect(Collectors.toList());
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @Operation(description = "Update 'Task'.")
    @GetMapping(value = "/edit/{id}")
    @PreAuthorize("hasAuthority('member')")
    // && @permissionCheck.checkTaskDate(#id)
    public String showUpdateForm(@Parameter(description = "Task ID", required = true)
                                 @PathVariable("id") final Long id, Model model) {
        model.addAttribute("task", mapper.map(taskService.getTask(id), TaskModel.class));
        model.addAttribute("activities", TaskActivity.values());
        return "update-task";
    }

    @Operation(description = "Update 'Task'.")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('member')")
    //&& @permissionCheck.checkTaskDate(#id)
    public String updateTask(@Parameter(description = "Task ID", required = true)
                             @PathVariable("id") final Long id,
                             @Validated TaskRq rq) {
        Task updated = taskService.update(id, taskMapper.toItem(rq));
        return "redirect:/tasks/issue/" + updated.getIssue().getId();
    }

    @Operation(description = "Delete 'Task' by id.")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('member')")
    //&& @permissionCheck.checkTaskDate(#id)
    public String deleteTask(@Parameter(description = "Task ID", required = true)
                             @PathVariable("id") Long id) {
        taskService.delete(id);
        return "redirect:/issues/assigned";
    }
}
