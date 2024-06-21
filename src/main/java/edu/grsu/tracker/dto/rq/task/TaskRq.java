package edu.grsu.tracker.dto.rq.task;

import edu.grsu.tracker.storage.common.task.TaskActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRq {

    private LocalDate date;

    private Integer hours;

    private String comment;

    private TaskActivity activity;
}
