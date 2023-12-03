package edu.grsu.tracker.model;

import edu.grsu.tracker.storage.common.user.UserRole;
import edu.grsu.tracker.storage.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Long id;

    private String name;

    private String surname;

    private String email;

    private UserRole role;

    private Set<Project> projects;
}
