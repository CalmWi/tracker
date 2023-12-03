package edu.grsu.tracker.model;

import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryModel {

    private Long id;

    private LocalDate updateDate;

    private String changes;

    private User user;

    private Issue issue;
}
