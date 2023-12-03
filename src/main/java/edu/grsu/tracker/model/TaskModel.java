package edu.grsu.tracker.model;

import edu.grsu.tracker.storage.common.task.TaskActivity;
import edu.grsu.tracker.storage.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskModel {
    private Long id;

    private LocalDate date;

    private Integer hours;

    private String comment;

    private LocalDate createOn;

    private Issue issue;

    private TaskActivity activity;
}
