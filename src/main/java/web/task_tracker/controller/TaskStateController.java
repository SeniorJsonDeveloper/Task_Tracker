package web.task_tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.task_tracker.dto.AskDto;
import web.task_tracker.dto.TaskDto;
import web.task_tracker.dto.TaskStateDto;
import web.task_tracker.entities.ProjectEntity;
import web.task_tracker.entities.TaskStateEntity;
import web.task_tracker.exception.BadRequestException;
import web.task_tracker.factory.TaskDtoFactory;
import web.task_tracker.factory.TaskStateDtoFactory;
import web.task_tracker.repository.TaskRepository;
import web.task_tracker.repository.TaskStateRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskStateController {
    private final TaskStateRepository taskRepository;
    private final TaskStateDtoFactory taskStateDtoFactory;
    private final ControllerHelper controllerHelper;
    private static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    private static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
    private static final String UPDATE_TASK_STATE = "/api/task-states/{task_state_id}";
    private static final String DELETE_TASK_STATE = "/api/task-states/{task_state_id}";



    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> fetchTack(@PathVariable(name = "projectId") Long projectId) {
        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);
        return project
                .getTaskStateEntities()
                .stream()
                .map(taskStateDtoFactory::taskFactory)
                .collect(Collectors.toList());

    }


    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(
            @PathVariable(name = "projectId") Long projectId,
            @RequestParam(value = "task_state_name") String taskName) {
        if (taskName.trim().isEmpty()) {
            throw new BadRequestException("task name can't be empty!");

        }
        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);
        Optional<TaskStateEntity> optionalAnotherTaskState = Optional.empty();
        for (TaskStateEntity entity : project.getTaskStateEntities()) {
            if (entity.getName().equalsIgnoreCase(taskName)) {
                throw new BadRequestException(String.format("Task \"%s\" already exists!", taskName));
            }
        }
        project.getTaskStateEntities()
                .stream()
                .map(TaskStateEntity::getName)
                .filter(anotherName -> anotherName.equalsIgnoreCase(taskName))
                .findAny()
                .ifPresent(it -> {
                    throw new BadRequestException(
                            String.format("Task state \"%s\" already exists", taskName));
                });

        final TaskStateEntity taskState = taskRepository
                .saveAndFlush(
                        TaskStateEntity.builder()
                                .name(taskName)
                                .build()

                );
        return taskStateDtoFactory.taskFactory(taskState);
    }

    @PatchMapping(UPDATE_TASK_STATE)
    public TaskStateDto updateTask(
            @PathVariable(name = "task_state_id") Long taskId,
            @RequestParam(value = "task_state_name") String taskName) {
        if (taskName.trim().isEmpty()) {
            throw new BadRequestException("task name can't be empty!");

        }
        TaskStateEntity taskStateEntity = getTaskStateOrThrowException(taskId);
        taskRepository
                .findTaskStateEntityByProjectIdAndNameContainsIgnoreCase
                        (taskStateEntity.getProject().getId(), taskName)
                .filter(anotherTaskState -> !anotherTaskState.getId().equals(taskId))
                .ifPresent(anotherTaskState -> {
                    throw new BadRequestException(String.format("Task state \"%s\" already exists!", taskName));
                });
        taskStateEntity.setName(taskName);
        taskStateEntity = taskRepository.saveAndFlush(taskStateEntity);
        TaskStateEntity taskState = taskRepository.saveAndFlush(taskStateEntity);
        return taskStateDtoFactory.taskFactory(taskState);
    }

    @DeleteMapping(DELETE_TASK_STATE)
    public AskDto deleteTask(@RequestParam Long taskId) {
        controllerHelper.getTaskOrThrowException(taskId);
        taskRepository.deleteById(taskId);
        return AskDto.makeAsk(true);
    }
    private TaskStateEntity getTaskStateOrThrowException(Long taskStateId){
        return taskRepository
                .findById(taskStateId)
                .orElseThrow(()->
                        new BadRequestException(
                                String.format(
                                        "Task state with \"%s\" id doesnt exists!"
                                        ,taskStateId)));
    }


}
