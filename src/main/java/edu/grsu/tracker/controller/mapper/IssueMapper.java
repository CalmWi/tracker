package edu.grsu.tracker.controller.mapper;

import edu.grsu.tracker.dto.rq.issue.IssueRq;
import edu.grsu.tracker.service.UserService;
import edu.grsu.tracker.storage.common.issue.IssueType;
import edu.grsu.tracker.storage.entity.Issue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class IssueMapper {
    private final UserService userService;

    public Issue toItem(final IssueRq rq) {
        return Issue.builder()
                .name(rq.getName())
                .assigned((rq.getAssigned() != null) ? userService.getUser(rq.getAssigned()) : null)
                .type(rq.getType())
                .description(rq.getDescription())
                .dueDate(rq.getDueDate())
                .createOn(LocalDate.now())
                .startDate(rq.getStartDate())
                .status(rq.getStatus())
                .priority(rq.getType() == IssueType.NOT_PTO
                        ? rq.getPriority() : null)
                .reason(rq.getType() == IssueType.PTO
                        ? rq.getReason() : null)
                .estimatedTime(rq.getType() == IssueType.NOT_PTO
                        ? rq.getEstimatedTime() : null)
                .tasks(Collections.emptyList())
                .build();
    }
}
