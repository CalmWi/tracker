package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.task.TaskRq;
import edu.grsu.tracker.rest.mapper.TaskMapper;
import edu.grsu.tracker.service.TaskService;
import edu.grsu.tracker.storage.entity.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper mapper;

    @Operation(description = "Get all 'Tasks'.")
    @GetMapping(value = "")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> taskList = taskService.getTasks();
        return ResponseEntity.ok(taskList);
    }

    @Operation(description = "Get 'Task' by id.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Task> getTask(@Parameter(description = "Task ID", required = true)
                                        @PathVariable("id") Long id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok(task);
    }

    @Operation(description = "Update 'Task'.")
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member') && @permissionCheck.checkTaskDate(#id)")
    public ResponseEntity<Task> updateTask(@Parameter(description = "Task ID", required = true)
                                           @PathVariable("id") final Long id,
                                           @RequestBody TaskRq rq) {

        Task update = taskService.update(id, mapper.toItem(rq));
        return ResponseEntity.ok().body(update);
    }

    @Operation(description = "Delete 'Task' by id.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member') && @permissionCheck.checkTaskDate(#id)")
    public ResponseEntity<Void> deleteTask(@Parameter(description = "Task ID", required = true)
                                           @PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
