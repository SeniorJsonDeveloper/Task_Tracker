package web.task_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskDto {
    private Boolean answer;
    public static AskDto makeAsk(boolean answer){
        return AskDto.builder()
                .answer(answer)
                .build();

    }
}
