package edu.grsu.tracker.api.rq.user;

import edu.grsu.tracker.storage.common.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRq {

    private String name;

    private String surname;

    private String password;

    private String email;

    private UserRole userRole;
}
