package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.issue.IssueRq;
import edu.grsu.tracker.api.rq.task.TaskRq;
import edu.grsu.tracker.model.IssueModel;
import edu.grsu.tracker.rest.mapper.IssueMapper;
import edu.grsu.tracker.rest.mapper.TaskMapper;
import edu.grsu.tracker.service.FileService;
import edu.grsu.tracker.service.IssueService;
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
    private final TaskMapper taskMapper;
    private final IssueMapper issueMapper;
    private final FileService fileService;
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
        model.addAttribute("issue", modelMapper.map(issue, IssueModel.class));
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
    @PostMapping(value = "/add/task/{issueId}")
    public ResponseEntity<IssueModel> addTaskToIssue(@Parameter(description = "Issue ID", required = true)
                                                     @PathVariable("issueId") final Long issueId,
                                                     @RequestBody TaskRq rq) {

        Issue added = issueService.addTaskToIssue(issueId, taskMapper.toItem(rq));
        return ResponseEntity.ok().body(modelMapper.map(added, IssueModel.class));
    }

    @Operation(description = "Update 'Issue'.")
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member') && @permissionCheck.checkIssueDate(#id)")
    public ResponseEntity<IssueModel> updateIssue(@Parameter(description = "Issue ID", required = true)
                                                  @PathVariable("id") final Long id,
                                                  @RequestBody IssueRq rq) {

        Issue update = issueService.update(id, issueMapper.toItem(rq));
        return ResponseEntity.ok().body(modelMapper.map(update, IssueModel.class));
    }

    @Operation(description = "Delete 'Issue' by id.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member') && @permissionCheck.checkIssueDate(#id)")
    public ResponseEntity<Void> deleteIssue(@Parameter(description = "Issue ID", required = true)
                                            @PathVariable("id") Long id) {
        issueService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Upload 'File' to issue.")
    @PostMapping(value = "/uploadFile/{issueId}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> upload(@Parameter(description = "Issue ID", required = true)
                                    @PathVariable("issueId") Long issueId,
                                    @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.addFile(issueId, file));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@Parameter(description = "Issue ID", required = true)
                                                      @PathVariable("id") Long id) throws IOException {
        LoadFile loadFile = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }
}
