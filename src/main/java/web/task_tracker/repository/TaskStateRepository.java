package web.task_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.task_tracker.entities.TaskStateEntity;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface TaskStateRepository extends JpaRepository<TaskStateEntity,Long> {
    Optional<TaskStateEntity> findByName(String name);
    Stream<TaskStateEntity> streamAllBy();
    Stream<TaskStateEntity> streamAllByNameStartsWithIgnoreCase(String name);
    Optional<TaskStateEntity> findTaskStateEntityByProjectIdAndNameContainsIgnoreCase(Long projectId,String taskStateName);
}
