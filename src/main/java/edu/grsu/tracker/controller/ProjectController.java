package edu.grsu.tracker.controller;

import edu.grsu.tracker.dto.rq.issue.IssueRq;
import edu.grsu.tracker.dto.rq.project.ProjectRq;
import edu.grsu.tracker.dto.rq.user.UserRq;
import edu.grsu.tracker.dto.model.ProjectModel;
import edu.grsu.tracker.dto.model.UserModel;
import edu.grsu.tracker.controller.mapper.IssueMapper;
import edu.grsu.tracker.controller.mapper.ProjectMapper;
import edu.grsu.tracker.service.ProjectService;
import edu.grsu.tracker.service.UserService;
import edu.grsu.tracker.storage.common.issue.IssuePriority;
import edu.grsu.tracker.storage.common.issue.IssueReason;
import edu.grsu.tracker.storage.common.issue.IssueStatus;
import edu.grsu.tracker.storage.common.issue.IssueType;
import edu.grsu.tracker.storage.common.project.ProjectStatus;
import edu.grsu.tracker.storage.common.project.ProjectType;
import edu.grsu.tracker.storage.entity.Project;
import edu.grsu.tracker.storage.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Projects")
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final IssueMapper issueMapper;
    private final ModelMapper mapper;

    @Operation(description = "Get all 'Projects'. ")
    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('write')")
    public String getAllProjects(Model model) {
        List<ProjectModel> projectList = projectService.getProjects().stream()
                .filter(project -> project.getStatus() != ProjectStatus.CLOSED)
                .map(project -> mapper.map(project, ProjectModel.class))
                .collect(Collectors.toList());
        model.addAttribute("projects", projectList);
        return "projects";
    }

    @Operation(description = "Get user 'Projects' by user_id ")
    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('member')")
    public String getProjects(@RequestAttribute("userId") Long userId, Model model) {
        List<ProjectModel> projectModels = projectService.getUserProjects(userId).stream()
                .filter(project -> project.getStatus() != ProjectStatus.CLOSED)
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
    public String getUserProjects(@Parameter(description = "User ID", required = true)
                                  @PathVariable("id") Long id, Model model) {
        List<ProjectModel> projectModels = projectService.getUserProjects(id).stream()
                .map(project -> mapper.map(project, ProjectModel.class))
                .collect(Collectors.toList());
        model.addAttribute("projects", projectModels);
        return "projects";
    }

    @Operation(description = "Get members of 'Project' by id.")
    @GetMapping(value = "/{id}/members")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<Set<User>> getProjectMembers(@Parameter(description = "Project ID", required = true)
                                                       @PathVariable("id") final Long id) {
        Set<User> members = projectService.getProjectMembers(id);
        return ResponseEntity.ok(members);
    }

    @Operation(description = "Get form of new 'Project'.")
    @GetMapping(value = "/add")
    @PreAuthorize("hasAuthority('write')")
    public String showNewProjectForm(Model model) {
        model.addAttribute("project", new ProjectRq());
        model.addAttribute("types", ProjectType.values());
        return "create-project";
    }

    @Operation(description = "Create a new 'Project'.")
    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('write')")
    public ModelAndView createProject(@Validated ProjectRq rq, BindingResult result) {
        Project created = projectService.save(projectMapper.toItem(rq));
        return new ModelAndView("project", "project", mapper.map(created, ProjectModel.class));
    }

    @Operation(description = "Get form of edit 'Project'.")
    @GetMapping(value = "/edit/{id}")
    @PreAuthorize("hasAuthority('edit')")
    public String showEditProjectForm(@PathVariable("id") final Long id, Model model) {
        model.addAttribute("project", projectService.getProject(id));
        model.addAttribute("types", ProjectType.values());
        model.addAttribute("statuses", ProjectStatus.values());
        return "update-project";
    }

    @Operation(description = "Update 'Project'.")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('edit')")
    public String updateProject(@Parameter(description = "Project ID", required = true)
                                @PathVariable("id") final Long id,
                                @Validated ProjectRq rq) {
        Project updated = projectService.update(id, projectMapper.toItem(rq));
        return "redirect:/projects/" + updated.getId();
    }

    @Operation(description = "Add 'Issue' to the 'Project'.")
    @GetMapping(value = "/add/issue/{projectId}")
    @PreAuthorize("hasAuthority('member')")
    public String showAddIssueToProjectForm(@Parameter(description = "Project ID", required = true)
                                            @PathVariable("projectId") final Long projectId, Model model) {
        model.addAttribute("issue", new IssueRq());
        model.addAttribute("types", IssueType.values());
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("priorities", IssuePriority.values());
        model.addAttribute("reasons", IssueReason.values());
        model.addAttribute("projectId", projectId);
        model.addAttribute("users",
                userService.getProjectUsers(projectId).stream()
                        .map(user -> mapper.map(user, UserModel.class))
                        .collect(Collectors.toList()));
        return "create-issue";
    }

    @Operation(description = "Add 'Issue' to the 'Project'.")
    @PostMapping(value = "/add/issue/{projectId}")
    @PreAuthorize("hasAuthority('member')")
    public String addIssueToProject(@Parameter(description = "Project ID", required = true)
                                    @PathVariable("projectId") final Long projectId,
                                    @Validated IssueRq rq) {
        Project added = projectService.addIssueToProject(projectId, issueMapper.toItem(rq), rq.getAssigned());
        return "redirect:/projects/" + added.getId();
    }

    @Operation(description = "Add 'User' to the 'Project'.")
    @GetMapping(value = "/add/member/{projectId}")
    @PreAuthorize("hasAuthority('edit')")
    public String showAddUserToProjectForm(@Parameter(description = "Project ID", required = true)
                                           @PathVariable("projectId") final Long projectId, Model model) {
        List<UserModel> users = userService.getUsers().stream()
                .map(user -> mapper.map(user, UserModel.class))
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("addedUser", new UserRq());
        model.addAttribute("projectId", projectId);
        return "add-user-to-project";
    }

    @Operation(description = "Add 'User' to the 'Project'.")
    @PostMapping(value = "/add/member/{projectId}")
    @PreAuthorize("hasAuthority('edit')")
    public String addUserToProject(@Parameter(description = "Project ID", required = true)
                                   @PathVariable("projectId") final Long projectId, @Validated final UserRq rq) {
        Project added = projectService.addUserToProject(rq.getId(), projectId);
        return "redirect:/projects/" + added.getId();
    }

    @Operation(description = "Delete 'Project' by id.")
    @GetMapping(value = "delete/{id}")
    @PreAuthorize("hasAuthority('write')")
    public String deleteProject(@Parameter(description = "Project ID", required = true)
                                @PathVariable("id") final Long id) {
        projectService.delete(id);
        return "redirect:/projects";
    }
}
