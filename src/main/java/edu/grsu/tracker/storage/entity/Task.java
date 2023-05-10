package edu.grsu.tracker.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.grsu.tracker.storage.common.task.TaskActivity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;

    private Integer hours;

    private String comment;

    private LocalDate createOn;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonBackReference
    private Issue issue;

    @Enumerated(EnumType.STRING)
    private TaskActivity activity;
}
