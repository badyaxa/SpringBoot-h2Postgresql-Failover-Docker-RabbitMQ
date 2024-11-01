package com.pr.tm.dto;

import com.pr.tm.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskPatchRequestDto {
    private String title;
    private String description;
    private TaskStatus status;
}
