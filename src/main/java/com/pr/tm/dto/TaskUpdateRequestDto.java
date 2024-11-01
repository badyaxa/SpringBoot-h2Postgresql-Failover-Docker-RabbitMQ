package com.pr.tm.dto;

import com.pr.tm.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateRequestDto extends TaskRequestDto {
    @NotNull(message = "Field 'status' is required")
    private TaskStatus status;

    @Override
    public String toString() {
        return "TaskUpdateRequestDto{"
                + super.toString()
                + ", status='" + status + "'}";
    }
}
