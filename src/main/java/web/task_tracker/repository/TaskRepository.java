package web.task_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.task_tracker.entities.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
}
