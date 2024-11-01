package com.pr.tm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDto {
    @NotBlank(message = "Field 'title' is required and should not be blank")
    @Size(max = 100, message = "The title of the task should not exceed 100 characters")
    private String title;

    @NotNull(message = "Field 'status' is required")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @Override
    public String toString() {
        return "title='" + title + "', description='" + description + "'";
    }
}
