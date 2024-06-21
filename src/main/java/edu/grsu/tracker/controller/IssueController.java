package edu.grsu.tracker.controller;

import edu.grsu.tracker.dto.rq.issue.IssueRq;
import edu.grsu.tracker.dto.rq.task.TaskRq;
import edu.grsu.tracker.dto.model.HistoryModel;
import edu.grsu.tracker.dto.model.IssueModel;
import edu.grsu.tracker.dto.model.UserModel;
import edu.grsu.tracker.controller.mapper.IssueMapper;
import edu.grsu.tracker.controller.mapper.TaskMapper;
import edu.grsu.tracker.service.FileService;
import edu.grsu.tracker.service.HistoryService;
import edu.grsu.tracker.service.IssueService;
import edu.grsu.tracker.service.UserService;
import edu.grsu.tracker.storage.common.issue.IssuePriority;
import edu.grsu.tracker.storage.common.issue.IssueReason;
import edu.grsu.tracker.storage.common.issue.IssueStatus;
import edu.grsu.tracker.storage.common.issue.IssueType;
import edu.grsu.tracker.storage.common.task.TaskActivity;
import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.LoadFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Issues")
@Controller
@RequiredArgsConstructor
@RequestMapping("/issues")
public class IssueController {
    private final IssueService issueService;
    private final HistoryService historyService;
    private final TaskMapper taskMapper;
    private final IssueMapper issueMapper;
    private final FileService fileService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Operation(description = "Get all 'Issues'.")
    @GetMapping(value = "")
    public ResponseEntity<List<IssueModel>> getIssues() {
        List<IssueModel> issueList = issueService.getIssues().stream()
                .map(issue -> modelMapper.map(issue, IssueModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(issueList);
    }

    @Operation(description = "Get 'Issue' by id.")
    @GetMapping(value = "/{id}")
    public String getIssue(@Parameter(description = "Issue ID", required = true)
                           @PathVariable("id") Long id, Model model) {
        Issue issue = issueService.getIssue(id);
        List<HistoryModel> issueHistories = historyService.getIssueHistories(id).stream()
                .map(history -> modelMapper.map(history, HistoryModel.class)).toList();
        List<LoadFile> files = fileService.getFilesByIssue(id);
        model.addAttribute("issue", modelMapper.map(issue, IssueModel.class));
        model.addAttribute("histories", issueHistories);
        model.addAttribute("files", files);
        return "issue";
    }

    @Operation(description = "Get assigned 'Issues' by user id.")
    @GetMapping(value = "/assigned")
    public String getAssignedIssues(@RequestAttribute("userId") Long userId, Model model) {
        List<IssueModel> issues = issueService.getAssignedIssues(userId).stream()
                .map(issue -> modelMapper.map(issue, IssueModel.class))
                .collect(Collectors.toList());
        model.addAttribute("issues", issues);
        return "issues";
    }

    @Operation(description = "Add 'Task' to the 'Issue'.")
    @GetMapping(value = "/add/task/{issueId}")
    public String showCreateTaskForm(@Parameter(description = "Issue ID", required = true)
                                     @PathVariable("issueId") final Long issueId, Model model) {
        model.addAttribute("task", new TaskRq());
        model.addAttribute("issueId", issueId);
        model.addAttribute("activities", TaskActivity.values());
        return "create-task";
    }

    @Operation(description = "Add 'Task' to the 'Issue'.")
    @PostMapping(value = "/add/task/{issueId}")
    @PreAuthorize("hasAuthority('member')")
    public String addTaskToIssue(@Parameter(description = "Issue ID", required = true)
                                 @PathVariable("issueId") final Long issueId,
                                 @RequestAttribute("userId") Long userId,
                                 @Validated TaskRq rq) {
        Issue added = issueService.addTaskToIssue(issueId, userId, taskMapper.toItem(rq));
        return "redirect:/issues/" + added.getId();
    }

    @Operation(description = "Update 'Task' from the 'Issue'.")
    @PostMapping(value = "/update/task/{taskId}")
    @PreAuthorize("hasAuthority('member')")
    public String updateTaskIssue(@Parameter(description = "Task ID", required = true)
                                  @PathVariable("taskId") final Long taskId,
                                  @Validated TaskRq rq) {
        Issue updated = issueService.updateTaskFromIssue(taskId, taskMapper.toItem(rq));
        return "redirect:/issues/" + updated.getId();
    }

    @Operation(description = "Delete 'Task' from the 'Issue'.")
    @GetMapping(value = "/delete/task/{taskId}")
    @PreAuthorize("hasAuthority('member')")
    public String updateTaskIssue(@Parameter(description = "Task ID", required = true)
                                  @PathVariable("taskId") final Long taskId) {
        Issue updated = issueService.deleteTaskFromIssue(taskId);
        return "redirect:/issues/" + updated.getId();
    }

    @Operation(description = "Get form of edit 'Issue'.")
    @GetMapping(value = "/edit/{id}")
    @PreAuthorize("hasAuthority('member')")
    public String showEditIssueForm(@PathVariable("id") final Long id, Model model) {
        Issue issue = issueService.getIssue(id);
        model.addAttribute("issue", modelMapper.map(issue, IssueModel.class));
        model.addAttribute("types", IssueType.values());
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("priorities", IssuePriority.values());
        model.addAttribute("reasons", IssueReason.values());
        model.addAttribute("users",
                userService.getProjectUsers(issue.getProject().getId()).stream()
                .map(user -> modelMapper.map(user, UserModel.class))
                .collect(Collectors.toList()));
        return "update-issue";
    }

    @Operation(description = "Update 'Issue'.")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('member')")
    //&& @permissionCheck.checkIssueDate(#id)
    public String updateIssue(@Parameter(description = "Issue ID", required = true)
                              @PathVariable("id") final Long id,
                              @Validated IssueRq rq, BindingResult result) {
        Issue updated = issueService.update(id, issueMapper.toItem(rq));
        return "redirect:/issues/" + updated.getId();
    }

    @Operation(description = "Delete 'Issue' by id.")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('member')")
    // && @permissionCheck.checkIssueDate(#id)
    public String deleteIssue(@Parameter(description = "Issue ID", required = true)
                              @PathVariable("id") Long id) {
        issueService.delete(id);
        return "redirect:/issues/assigned";
    }

    @GetMapping("/file/{issueId}")
    public String showUploadFileForm(@Parameter(description = "Issue ID", required = true)
                                     @PathVariable("issueId") Long issueId, Model model) {
        model.addAttribute("issueId", issueId);
        return "upload-file";
    }

    @Operation(description = "Upload 'File' to issue.")
    @PostMapping(value = "/uploadFile/{issueId}", consumes = {"multipart/form-data"})
    public String upload(@Parameter(description = "Issue ID", required = true)
                         @PathVariable("issueId") Long issueId,
                         @RequestParam("file") MultipartFile file) throws IOException {
        fileService.addFile(issueId, file);
        return "redirect:/issues/" + issueId;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@Parameter(description = "Issue ID", required = true)
                                                      @PathVariable("id") String id) throws IOException {
        LoadFile loadFile = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }
}
