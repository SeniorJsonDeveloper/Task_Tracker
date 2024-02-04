package web.task_tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import web.task_tracker.entities.ProjectEntity;
import web.task_tracker.entities.TaskStateEntity;
import web.task_tracker.exception.BadRequestException;
import web.task_tracker.repository.ProjectRepository;
import web.task_tracker.repository.TaskStateRepository;

@Component
@RequiredArgsConstructor
public class ControllerHelper {
    private final ProjectRepository projectRepository;
    private final TaskStateRepository taskStateRepository;

    public ProjectEntity getProjectOrThrowException(Long projectId) {

        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new BadRequestException(
                                String.format(
                                        "Project with \"%s\" doesn't exist.",
                                        projectId
                                )
                        )
                );
    }
    public TaskStateEntity getTaskOrThrowException(Long taskId){
        return taskStateRepository
                .findById(taskId)
                .orElseThrow(()->
                        new BadRequestException(
                                String.format(
                                        "Project with \"%s\" doesn't exist.",
                                        taskId
                                )
                        )
                );
    }
}
