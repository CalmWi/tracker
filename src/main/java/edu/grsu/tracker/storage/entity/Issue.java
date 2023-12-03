package edu.grsu.tracker.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.grsu.tracker.storage.common.issue.IssuePriority;
import edu.grsu.tracker.storage.common.issue.IssueReason;
import edu.grsu.tracker.storage.common.issue.IssueStatus;
import edu.grsu.tracker.storage.common.issue.IssueType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private IssueType type;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Enumerated(EnumType.STRING)
    private IssuePriority priority;

    @Enumerated(EnumType.STRING)
    private IssueReason reason;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User assigned;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    private LocalDate startDate;

    private LocalDate dueDate;

    private LocalDate createOn;

    private Integer estimatedTime;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Task> tasks;

    @OneToOne(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonManagedReference
    private History history;
}
