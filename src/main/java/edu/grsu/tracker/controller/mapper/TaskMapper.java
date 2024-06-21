package edu.grsu.tracker.controller.mapper;

import edu.grsu.tracker.dto.rq.task.TaskRq;
import edu.grsu.tracker.storage.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskMapper {

    public Task toItem(final TaskRq rq) {
        return Task.builder()
                .date(rq.getDate())
                .activity(rq.getActivity())
                .hours(rq.getHours())
                .createOn(LocalDate.now())
                .comment(rq.getComment())
                .build();
    }

}
