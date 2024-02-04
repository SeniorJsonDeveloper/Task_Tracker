package web.task_tracker.factory;

import org.springframework.stereotype.Component;
import web.task_tracker.dto.TaskDto;
import web.task_tracker.entities.TaskEntity;

@Component
public class TaskDtoFactory {
    public TaskDto taskFactory(TaskEntity task){
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
