package edu.grsu.tracker.rest;

import edu.grsu.tracker.api.rq.issue.IssueRq;
import edu.grsu.tracker.api.rq.task.TaskRq;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Issues")
@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;
    private final TaskMapper taskMapper;
    private final IssueMapper issueMapper;
    private final FileService fileService;

    @Operation(description = "Get all 'Issues'.")
    @GetMapping(value = "")
    public ResponseEntity<List<Issue>> getIssues() {
        List<Issue> issueList = issueService.getIssues();
        return ResponseEntity.ok(issueList);
    }

    @Operation(description = "Get 'Issue' by id.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Issue> getIssue(@Parameter(description = "Issue ID", required = true)
                                          @PathVariable("id") Long id) {
        Issue issue = issueService.getIssue(id);
        return ResponseEntity.ok(issue);
    }

    @Operation(description = "Get assigned 'Issues' by user id.")
    @GetMapping(value = "/assigned/{id}")
    public ResponseEntity<List<Issue>> getAssignedIssues(@Parameter(description = "User ID", required = true)
                                                         @PathVariable("id") Long id) {
        List<Issue> issues = issueService.getAssignedIssues(id);
        return ResponseEntity.ok(issues);
    }

    @Operation(description = "Add 'Task' to the 'Issue'.")
    @PostMapping(value = "/add/task/{issueId}")
    public ResponseEntity<Issue> addTaskToIssue(@Parameter(description = "Issue ID", required = true)
                                                @PathVariable("issueId") final Long issueId,
                                                @RequestBody TaskRq rq) {

        Issue added = issueService.addTaskToIssue(issueId, taskMapper.toItem(rq));
        return ResponseEntity.ok().body(added);
    }

    @Operation(description = "Update 'Issue'.")
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member') && @permissionCheck.checkIssueDate(#id)")
    public ResponseEntity<Issue> updateIssue(@Parameter(description = "Issue ID", required = true)
                                             @PathVariable("id") final Long id,
                                             @RequestBody IssueRq rq) {

        Issue update = issueService.update(id, issueMapper.toItem(rq));
        return ResponseEntity.ok().body(update);
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