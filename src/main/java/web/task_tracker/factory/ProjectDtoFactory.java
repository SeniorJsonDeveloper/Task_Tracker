package web.task_tracker.factory;

import org.springframework.stereotype.Component;
import web.task_tracker.dto.ProjectDto;
import web.task_tracker.entities.ProjectEntity;

@Component
public class ProjectDtoFactory {
    public ProjectDto projectFactory(ProjectEntity project){
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
