package web.task_tracker.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String description;
    @Column(unique = true)
    private String name;
    @CreationTimestamp
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant updatedAt = Instant.now();
    private Long ordinal;
    @OneToMany
    @Builder.Default
    @JoinColumn(name = "task_state_id")
    private List<TaskEntity> taskEntities = new ArrayList<>();
    @ManyToOne
    private ProjectEntity project;

}
