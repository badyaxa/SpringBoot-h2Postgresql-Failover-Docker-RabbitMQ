package com.pr.tm.controller;

import com.pr.tm.dto.TaskCreateRequestDto;
import com.pr.tm.dto.TaskPatchRequestDto;
import com.pr.tm.dto.TaskResponseDto;
import com.pr.tm.dto.TaskUpdateRequestDto;
import com.pr.tm.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create a new task",
            description = "Create a new task with the specified title, description, and status",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            },
            parameters = {
                    @Parameter(name = "title", required = true, example = "Task 1",
                            description = "Task title"),
                    @Parameter(name = "description", required = true, example = "Task 1 description",
                            description = "Task description"),
                    @Parameter(name = "status", example = "NEW",
                            description = "Task status \"NEW\", \"IN_PROGRESS\", \"COMPLETED\" (default value = \"NEW\")")
            }
    )
    public ResponseEntity<Long> create(@Valid @RequestBody TaskCreateRequestDto dto) {
        log.info("Creating a new task: {}", dto);
        Long createdId = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task",
            description = "Delete a task by its ID",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task deleted"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = {
                    @Parameter(name = "id", required = true, example = "1", description = "Task ID")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting task by id: {}", id);
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task",
            description = "Update a task by its ID",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = {
                    @Parameter(name = "id", required = true, example = "1",
                            description = "Task ID"),
                    @Parameter(name = "title", required = true, example = "Task 1",
                            description = "Task title"),
                    @Parameter(name = "description", required = true, example = "Task 1 description",
                            description = "Task description"),
                    @Parameter(name = "status", required = true, example = "NEW",
                            description = "Task status \"NEW\", \"IN_PROGRESS\", \"COMPLETED\"")
            }
    )
    public ResponseEntity<TaskResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequestDto dto) {
        log.info("Updating task by id: {} with data: {}", id, dto);
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch a task",
            description = "Patch a task by its ID",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task patched"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = {
                    @Parameter(name = "id", required = true, example = "1",
                            description = "Task ID"),
                    @Parameter(name = "title",
                            description = "Task title", example = "Task 1"),
                    @Parameter(name = "description", example = "Task 1 description",
                            description = "Task description"),
                    @Parameter(name = "status", example = "NEW",
                            description = "Task status \"NEW\", \"IN_PROGRESS\", \"COMPLETED\" (default value = \"NEW\")")
            }
    )
    public ResponseEntity<TaskResponseDto> patch(
            @PathVariable Long id,
            @Valid @RequestBody TaskPatchRequestDto dto) {
        log.info("Patching task by id: {} with data: {}", id, dto);
        return ResponseEntity.ok(taskService.patch(id, dto));
    }

    @GetMapping
    @Operation(summary = "Get all tasks",
            description = "Get a list of all tasks",
            tags = {"tasks"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all tasks")
            }
    )
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        log.info("Getting all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID",
            description = "Get a task by its ID",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task found"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        log.info("Getting task by id: {}", id);
        TaskResponseDto task = taskService.getTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound()
                    .build();
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}/in-progress")
    @Operation(summary = "Start a task",
            description = "Start a task by its ID",
            tags = {"task-status-change"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task in progress"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    public ResponseEntity<TaskResponseDto> inProgressTask(@PathVariable Long id) {
        log.info("Starting task by id: {}", id);
        return ResponseEntity.ok(taskService.inProgress(id));
    }

    @GetMapping("/{id}/complete")
    @Operation(summary = "Complete a task",
            description = "Complete a task by its ID",
            tags = {"task-status-change"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task completed"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            parameters = {
                    @Parameter(name = "id", description = "Task ID", required = true, example = "1")
            }
    )
    public ResponseEntity<TaskResponseDto> completeTask(@PathVariable Long id) {
        log.info("Completing task by id: {}", id);
        return ResponseEntity.ok(taskService.complete(id));
    }
}
