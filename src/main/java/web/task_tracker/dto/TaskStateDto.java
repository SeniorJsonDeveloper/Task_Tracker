package web.task_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStateDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private Long ordinal;
    @NonNull
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
    @NonNull
    List<TaskDto> tasks = new ArrayList<>();

}
