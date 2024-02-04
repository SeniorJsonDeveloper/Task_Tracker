package web.task_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.task_tracker.controller.ControllerHelper;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;
    private final ControllerHelper controllerHelper;
    @Override
    public List<ProjectDto> fetchProject(Optional<String> optionalName ) {
         optionalName  = optionalName.filter(p->!p.trim().isEmpty());
        Stream<ProjectEntity> entity = optionalName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);
        return entity.map(projectDtoFactory::projectFactory)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto createProject(Optional<Long> optionalProjectId, Optional<String> optionalProjectName) {
        optionalProjectName = optionalProjectName.filter(projectName->!projectName.trim().isEmpty());
        boolean isCreated = !optionalProjectId.isPresent();
        if (isCreated && !optionalProjectName.isPresent()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        final ProjectEntity project = optionalProjectId
                .map(controllerHelper::getProjectOrThrowException)
                .orElseGet(()->ProjectEntity.builder().build());


        optionalProjectName
                .ifPresent(projectName->{
                    projectRepository
                            .findByName(projectName)
                            .filter(anotherProject->!Objects.equals(anotherProject.getId(),project.getId()))
                            .ifPresent(anotherProject->{
                                throw new BadRequestException("");
                            });
                    project.setName(projectName);
                });
        final ProjectEntity finalProject = projectRepository.saveAndFlush(project);
        return projectDtoFactory.projectFactory(finalProject);
    }

    @Override
    public AskDto deleteProject(Long projectId) {
         projectRepository
                .findById(projectId)
                .orElseThrow(
                        ()->new BadRequestException
                                ("Project with this projectId not found!"));
        projectRepository.deleteById(projectId);
        return AskDto.makeAsk(true);
    }
}
