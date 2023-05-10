package edu.grsu.tracker.api.rq.issue;

import edu.grsu.tracker.storage.common.issue.IssuePriority;
import edu.grsu.tracker.storage.common.issue.IssueReason;
import edu.grsu.tracker.storage.common.issue.IssueStatus;
import edu.grsu.tracker.storage.common.issue.IssueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueRq {

    private String name;

    private String description;

    private IssueType type;

    private IssueStatus status;

    private IssuePriority priority;

    private IssueReason reason;

    private LocalDate startDate;

    private LocalDate dueDate;

    private Integer estimatedTime;

    private Long userId;
}
