package web.task_tracker.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import web.task_tracker.dto.TaskDto;
import web.task_tracker.dto.TaskStateDto;
import web.task_tracker.entities.TaskStateEntity;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskStateDtoFactory {
    private TaskDtoFactory taskDtoFactory;
    public TaskStateDto taskFactory(TaskStateEntity taskState){
        return TaskStateDto.builder()
                .id(taskState.getId())
                .name(taskState.getName())
                .createdAt(taskState.getCreatedAt())
                .ordinal(taskState.getOrdinal())
                .tasks(
                        taskState
                                .getTaskEntities()
                                .stream()
                                .map(taskDtoFactory::taskFactory)
                                .collect(Collectors.toList())
                )
                .build();
    }

}
