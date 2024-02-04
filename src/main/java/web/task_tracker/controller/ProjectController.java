package web.task_tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.task_tracker.dto.AskDto;
import web.task_tracker.dto.ProjectDto;
import web.task_tracker.entities.ProjectEntity;
import web.task_tracker.exception.BadRequestException;
import web.task_tracker.factory.ProjectDtoFactory;
import web.task_tracker.repository.ProjectRepository;
import web.task_tracker.service.ProjectService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/project")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final ControllerHelper controllerHelper;
    private final ProjectDtoFactory projectDtoFactory;
    private final ProjectService projectService;
    @GetMapping
    public List<ProjectDto> fetchProject(
            @RequestParam(value = "prefixName",required = false) Optional<String> optionalName){
        optionalName  = optionalName.filter(p->!p.trim().isEmpty());
        Stream<ProjectEntity> entity = optionalName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);
        return entity.map(projectDtoFactory::projectFactory)
                .collect(Collectors.toList());
    }

    @PutMapping
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id",required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name",required = false) Optional<String> optionalProjectName) {
        {
            optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());
            boolean isCreated = !optionalProjectId.isPresent();
            if (isCreated && !optionalProjectName.isPresent()) {
                throw new BadRequestException("Project name can't be empty.");
            }

            final ProjectEntity project = optionalProjectId
                    .map(controllerHelper::getProjectOrThrowException)
                    .orElseGet(() -> ProjectEntity.builder().build());


            optionalProjectName
                    .ifPresent(projectName -> {
                        projectRepository
                                .findByName(projectName)
                                .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                                .ifPresent(anotherProject -> {
                                    throw new BadRequestException("");
                                });
                        project.setName(projectName);
                    });
            final ProjectEntity finalProject = projectRepository.saveAndFlush(project);
            return projectDtoFactory.projectFactory(finalProject);

        }
    }
    @DeleteMapping("/{projectId}")
    public AskDto deleteProject(@PathVariable Long projectId){
        controllerHelper.getProjectOrThrowException(projectId);
        projectService.deleteProject(projectId);
        return AskDto.makeAsk(true);
    }

}

