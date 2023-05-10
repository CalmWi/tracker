package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.issue.IssueRq;
import edu.grsu.tracker.api.rq.project.ProjectRq;
import edu.grsu.tracker.rest.mapper.IssueMapper;
import edu.grsu.tracker.rest.mapper.ProjectMapper;
import edu.grsu.tracker.service.ProjectService;
import edu.grsu.tracker.storage.entity.Project;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Projects")
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final IssueMapper issueMapper;

    @Operation(description = "Get all 'Projects'. ")
    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projectList = projectService.getProjects();
        return ResponseEntity.ok(projectList);
    }

    @Operation(description = "Get 'Project' by id.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Project> getProject(@Parameter(description = "Project ID", required = true)
                                              @PathVariable("id") final Long id) {
        Project project = projectService.getProject(id);
        return ResponseEntity.ok(project);
    }

    @Operation(description = "Get user 'Projects' by id.")
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<Project>> getUserProjects(@Parameter(description = "User ID", required = true)
                                                         @PathVariable("id") Long id) {
        List<Project> issues = projectService.getUserProjects(id);
        return ResponseEntity.ok(issues);
    }

    @Operation(description = "Get members of 'Project' by id.")
    @GetMapping(value = "/{id}/members")
    public ResponseEntity<Set<User>> getProjectMembers(@Parameter(description = "Project ID", required = true)
                                                       @PathVariable("id") final Long id) {
        Set<User> members = projectService.getProjectMembers(id);
        return ResponseEntity.ok(members);
    }

    @Operation(description = "Create a new 'Project'.")
    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<Project> createProject(@RequestBody ProjectRq rq) {
        Project created = projectService.save(projectMapper.toItem(rq));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(description = "Update 'Project'.")
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<Project> updateProject(@Parameter(description = "Project ID", required = true)
                                                 @PathVariable("id") final Long id,
                                                 @RequestBody ProjectRq rq) {

        Project update = projectService.update(id, projectMapper.toItem(rq));
        return ResponseEntity.ok().body(update);
    }

    @Operation(description = "Add 'Issue' to the 'Project'.")
    @PostMapping(value = "/add/issue/{projectId}")
    @PreAuthorize("hasAuthority('member') && @permissionCheck.checkTypeProject(#projectId)")
    public ResponseEntity<Project> addIssueToProject(@Parameter(description = "Project ID", required = true)
                                                     @PathVariable("projectId") final Long projectId,
                                                     @RequestBody IssueRq rq) {

        Project added = projectService.addIssueToProject(projectId, issueMapper.toItem(rq), rq.getUserId());
        return ResponseEntity.ok().body(added);
    }

    @Operation(description = "Add 'User' to the 'Project'.")
    @PostMapping(value = "/{userId}/add/member/{projectId}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<Project> addUserToProject(@Parameter(description = "User ID", required = true)
                                                    @PathVariable("userId") final Long userId,
                                                    @Parameter(description = "Project ID", required = true)
                                                    @PathVariable("projectId") final Long projectId) {
        Project added = projectService.addUserToProject(userId, projectId);
        return ResponseEntity.ok().body(added);
    }

    @Operation(description = "Delete 'Project' by id.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<Void> deleteProject(@Parameter(description = "Project ID", required = true)
                                              @PathVariable("id") final Long id) {
        projectService.delete(id);
        return ResponseEntity.ok().build();
    }
}
