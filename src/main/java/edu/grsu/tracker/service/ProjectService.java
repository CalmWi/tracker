package edu.grsu.tracker.service;

import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.storage.entity.Issue;
import edu.grsu.tracker.storage.entity.Project;
import edu.grsu.tracker.storage.entity.User;
import edu.grsu.tracker.storage.repo.ProjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final UserService userService;
    private final IssueService issueService;

    public Project getProject(final Long id) {
        return projectRepo.findById(id).orElseThrow(() ->
                new TrackerExceptoin("Project not found")
        );
    }

    public Set<User> getProjectMembers(final Long id) {
        return getProject(id).getMembers();
    }

    public List<Project> getProjects() {
        return projectRepo.findAll();
    }

    public List<Project> getUserProjects(final Long userId) {
        return projectRepo.findAllByMember(userId);
    }

    public Project save(final Project project) {
        return projectRepo.save(project);
    }

    public Project update(final Long id, Project project) {
        Project get = getProject(id);
        get.setName(project.getName());
        get.setType(project.getType());
        return projectRepo.save(get);
    }

    public void delete(final Long id) {
        if (!getProject(id).getIssues().isEmpty()) {
            throw new TrackerExceptoin("Can't delete project. ",
                    "Project has linked issues");
        }
        projectRepo.deleteById(id);
    }

    @Transactional
    public Project addIssueToProject(final Long projectId, final Issue issue, final Long userId) {
        Project project = getProject(projectId);

        User user = userService.getUser(userId);
        project.getIssues().add(issue);

        issue.setAssigned(user);
        issue.setProject(project);
        issueService.save(issue);

        return projectRepo.save(project);
    }

    @Transactional
    public Project addUserToProject(final Long userId, final Long projectId) {
        User user = userService.getUser(userId);
        Project project = getProject(projectId);

        user.getProjects().add(project);
        project.getMembers().add(user);
        userService.save(user);

        return project;
    }
}
