package com.pr.tm.mapper;

import com.pr.tm.dto.TaskCreateRequestDto;
import com.pr.tm.dto.TaskResponseDto;
import com.pr.tm.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", expression = "java(dto.getTitle().trim())")
    @Mapping(target = "description", expression = "java(dto.getDescription().trim())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toTask(TaskCreateRequestDto dto);

    TaskResponseDto toResponse(Task task);
}
