package web.task_tracker.service;

import web.task_tracker.dto.AskDto;
import web.task_tracker.dto.ProjectDto;
import web.task_tracker.entities.ProjectEntity;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<ProjectDto> fetchProject(Optional<String> optionalName);
    ProjectDto createProject(Optional<Long> optionalProjectId,
                             Optional<String> optionalProjectName);
    AskDto deleteProject(Long projectId);
}
