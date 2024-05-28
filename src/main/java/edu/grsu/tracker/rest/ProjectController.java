package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.issue.IssueRq;
import edu.grsu.tracker.api.rq.project.ProjectRq;
import edu.grsu.tracker.model.ProjectModel;
import edu.grsu.tracker.rest.mapper.IssueMapper;
import edu.grsu.tracker.rest.mapper.ProjectMapper;
import edu.grsu.tracker.service.ProjectService;
import edu.grsu.tracker.storage.entity.Project;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Projects")
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final IssueMapper issueMapper;
    private final ModelMapper mapper;

    @Operation(description = "Get all 'Projects'. ")
    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<List<ProjectModel>> getAllProjects() {
        List<ProjectModel> projectList = projectService.getProjects().stream()
                .map(project -> mapper.map(project, ProjectModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectList);
    }

    @Operation(description = "Get user 'Projects' by user_id ")
    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('member')")
    public String getProjects(@RequestAttribute("userId") Long userId, Model model) {
        List<ProjectModel> projectModels = projectService.getUserProjects(userId).stream()
                .map(project -> mapper.map(project, ProjectModel.class))
                .collect(Collectors.toList());
        model.addAttribute("projects", projectModels);
        return "projects";
    }

    @Operation(description = "Get 'Project' by id.")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member')")
    public String getProject(@Parameter(description = "Project ID", required = true)
                                                   @PathVariable("id") final Long id, Model model) {
        Project project = projectService.getProject(id);
        model.addAttribute("project", mapper.map(project, ProjectModel.class));
        return "project";
    }

    @Operation(description = "Get user 'Projects' by id.")
    @GetMapping(value = "/user/{id}")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<List<ProjectModel>> getUserProjects(@Parameter(description = "User ID", required = true)
                                                              @PathVariable("id") Long id) {
        List<ProjectModel> projectModels = projectService.getUserProjects(id).stream()
                .map(project -> mapper.map(project, ProjectModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectModels);
    }

    @Operation(description = "Get members of 'Project' by id.")
    @GetMapping(value = "/{id}/members")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<Set<User>> getProjectMembers(@Parameter(description = "Project ID", required = true)
                                                       @PathVariable("id") final Long id) {
        Set<User> members = projectService.getProjectMembers(id);
        return ResponseEntity.ok(members);
    }

    @Operation(description = "Create a new 'Project'.")
    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<ProjectModel> createProject(@RequestBody ProjectRq rq) {
        Project created = projectService.save(projectMapper.toItem(rq));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(created, ProjectModel.class));
    }

    @Operation(description = "Update 'Project'.")
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<ProjectModel> updateProject(@Parameter(description = "Project ID", required = true)
                                                      @PathVariable("id") final Long id,
                                                      @RequestBody ProjectRq rq) {

        Project update = projectService.update(id, projectMapper.toItem(rq));
        return ResponseEntity.ok().body(mapper.map(update, ProjectModel.class));
    }

    @Operation(description = "Add 'Issue' to the 'Project'.")
    @PostMapping(value = "/add/issue/{projectId}")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<ProjectModel> addIssueToProject(@Parameter(description = "Project ID", required = true)
                                                          @PathVariable("projectId") final Long projectId,
                                                          @RequestBody IssueRq rq) {

        Project added = projectService.addIssueToProject(projectId, issueMapper.toItem(rq), rq.getUserId());
        return ResponseEntity.ok().body(mapper.map(added, ProjectModel.class));
    }

    @Operation(description = "Add 'User' to the 'Project'.")
    @PostMapping(value = "/{userId}/add/member/{projectId}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<ProjectModel> addUserToProject(@Parameter(description = "User ID", required = true)
                                                         @PathVariable("userId") final Long userId,
                                                         @Parameter(description = "Project ID", required = true)
                                                         @PathVariable("projectId") final Long projectId) {
        Project added = projectService.addUserToProject(userId, projectId);
        return ResponseEntity.ok().body(mapper.map(added, ProjectModel.class));
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
