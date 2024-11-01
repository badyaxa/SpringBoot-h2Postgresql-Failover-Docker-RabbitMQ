package com.pr.tm.dto;

import com.pr.tm.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateRequestDto extends TaskRequestDto {
    private TaskStatus status;

    @Override
    public String toString() {
        return "TaskCreateRequestDto{"
                + super.toString()
                + ", status='" + status + "'}";
    }
}
