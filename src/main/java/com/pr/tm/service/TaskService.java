package com.pr.tm.service;

import com.pr.tm.dto.TaskCreateRequestDto;
import com.pr.tm.dto.TaskPatchRequestDto;
import com.pr.tm.dto.TaskResponseDto;
import com.pr.tm.dto.TaskUpdateRequestDto;
import com.pr.tm.exception.NotFoundException;
import com.pr.tm.mapper.TaskMapper;
import com.pr.tm.model.Task;
import com.pr.tm.model.TaskStatus;
import com.pr.tm.repository.TaskRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {
    private static final long MAXIMUM_TASKS_COUNT = 100;
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskMessageService messageService;

    @Autowired
    public TaskService(TaskRepository repository,
                       TaskMapper mapper,
                       TaskMessageService messageService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.messageService = messageService;
    }

    public Long create(TaskCreateRequestDto dto) {
        log.info("Creating a new task: {}", dto);
        Task task = mapper.toTask(dto);

        checkTaskWithSameTitle(task.getTitle());
        checkMaximumNumberActiveTasks();

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.NEW);
        }
        messageService.sendTaskCreatedMessage(task);
        return repository.save(task).getId();
    }

    public void delete(Long id) {
        log.info("Deleting task by id: {}", id);
        repository.delete(findById(id));
    }

    public TaskResponseDto update(Long id, TaskUpdateRequestDto dto) {
        log.info("Updating task by id: {} with data: {}", id, dto);
        String title = dto.getTitle().trim();
        checkTaskWithSameTitle(title);

        Task task = findById(id);
        task.setTitle(title);
        task.setDescription(dto.getDescription().trim());
        task.setStatus(dto.getStatus());
        return update(task);
    }

    public TaskResponseDto patch(Long id, @Valid TaskPatchRequestDto dto) {
        log.info("Patching task by id: {} with data: {}", id, dto);
        Task task = findById(id);

        if (dto.getTitle() != null) {
            String title = dto.getTitle().trim();
            checkTaskWithSameTitle(title);
            task.setTitle(title);
        }

        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription().trim());
        }

        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        }
        return update(task);
    }

    public TaskResponseDto inProgress(Long id) {
        log.info("Starting task by id: {}", id);
        Task notCompletedTask = findNotCompleted(id);
        notCompletedTask.setStatus(TaskStatus.IN_PROGRESS);
        return update(notCompletedTask);
    }

    public TaskResponseDto complete(Long id) {
        log.info("Completing task by id: {}", id);
        Task notCompletedTask = findNotCompleted(id);
        notCompletedTask.setStatus(TaskStatus.COMPLETED);
        return update(notCompletedTask);
    }

    public List<TaskResponseDto> getAllTasks() {
        log.info("Getting all tasks");
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public TaskResponseDto getTaskById(Long id) {
        log.info("Getting task by id: {}", id);
        return mapper.toResponse(findById(id));
    }

    private void checkTaskWithSameTitle(String title) {
        repository.findByTitleAndStatusNot(title, TaskStatus.COMPLETED)
                .ifPresent(task -> {
                    throw new IllegalArgumentException("Task with this title already exists: "
                            + title);
                });
    }

    private void checkMaximumNumberActiveTasks() {
        long activeTasksCount = repository.countByStatusNot(TaskStatus.COMPLETED);
        if (activeTasksCount >= MAXIMUM_TASKS_COUNT) {
            throw new IllegalArgumentException("The maximum number of tasks ({}) is reached"
                    + MAXIMUM_TASKS_COUNT);
        }
    }

    private Task findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Task with this id = " + id + " was not found.")
        );
    }

    private Task findNotCompleted(Long id) {
        return repository.findByIdAndStatusNot(id, TaskStatus.COMPLETED)
                .orElseThrow(() -> new NotFoundException("Not completed task with id = "
                        + id + " was not found."));
    }

    private TaskResponseDto update(Task task) {
        return mapper.toResponse(repository.save(task));
    }
}
