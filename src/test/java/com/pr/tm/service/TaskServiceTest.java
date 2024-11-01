package com.pr.tm.service;

import com.pr.tm.dto.TaskCreateRequestDto;
import com.pr.tm.dto.TaskPatchRequestDto;
import com.pr.tm.dto.TaskResponseDto;
import com.pr.tm.dto.TaskUpdateRequestDto;
import com.pr.tm.mapper.TaskMapper;
import com.pr.tm.model.Task;
import com.pr.tm.model.TaskStatus;
import com.pr.tm.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMessageService messageService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        TaskCreateRequestDto dto = new TaskCreateRequestDto();
        dto.setTitle("Mock Task Title");
        dto.setDescription("Mock Task Description");

        Task task = new Task();
        task.setId(1L);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(TaskStatus.NEW);

        when(taskMapper.toTask(any(TaskCreateRequestDto.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Long createdTaskId = taskService.create(dto);

        assertNotNull(createdTaskId);
        assertEquals(1L, createdTaskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void delete() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.delete(taskId);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void update_task() {
        Long taskId = 1L;
        TaskUpdateRequestDto dto = new TaskUpdateRequestDto();
        dto.setTitle("Updated Task Title");
        dto.setDescription("Updated Task Description");
        dto.setStatus(TaskStatus.IN_PROGRESS);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task Title");
        existingTask.setDescription("Old Task Description");
        existingTask.setStatus(TaskStatus.NEW);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(new TaskResponseDto());

        TaskResponseDto response = taskService.update(taskId, dto);

        assertNotNull(response);
        assertEquals("Updated Task Title", existingTask.getTitle());
        assertEquals("Updated Task Description", existingTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, existingTask.getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void update_title() {
        Long taskId = 1L;
        TaskPatchRequestDto dto = new TaskPatchRequestDto();
        dto.setTitle("Patched Task Title");

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task Title");
        existingTask.setDescription("Old Task Description");
        existingTask.setStatus(TaskStatus.NEW);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(new TaskResponseDto());

        TaskResponseDto response = taskService.patch(taskId, dto);

        assertNotNull(response);
        assertEquals("Patched Task Title", existingTask.getTitle());
        assertEquals("Old Task Description", existingTask.getDescription());
        assertEquals(TaskStatus.NEW, existingTask.getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void update_description() {
        Long taskId = 1L;
        TaskPatchRequestDto dto = new TaskPatchRequestDto();
        dto.setDescription("Patched Task Description");

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task Title");
        existingTask.setDescription("Old Task Description");
        existingTask.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(new TaskResponseDto());

        TaskResponseDto response = taskService.patch(taskId, dto);

        assertNotNull(response);
        assertEquals("Old Task Title", existingTask.getTitle());
        assertEquals("Patched Task Description", existingTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, existingTask.getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void update_statusInProgress() {
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(TaskStatus.NEW);

        when(taskRepository.findByIdAndStatusNot(taskId, TaskStatus.COMPLETED)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(new TaskResponseDto());

        TaskResponseDto response = taskService.inProgress(taskId);

        assertNotNull(response);
        assertEquals(TaskStatus.IN_PROGRESS, existingTask.getStatus());
        verify(taskRepository, times(1)).findByIdAndStatusNot(taskId, TaskStatus.COMPLETED);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void update_statusComplete() {
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findByIdAndStatusNot(taskId, TaskStatus.COMPLETED)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(new TaskResponseDto());

        TaskResponseDto response = taskService.complete(taskId);

        assertNotNull(response);
        assertEquals(TaskStatus.COMPLETED, existingTask.getStatus());
        verify(taskRepository, times(1)).findByIdAndStatusNot(taskId, TaskStatus.COMPLETED);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void getAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
        when(taskMapper.toResponse(task1)).thenReturn(new TaskResponseDto());
        when(taskMapper.toResponse(task2)).thenReturn(new TaskResponseDto());

        List<TaskResponseDto> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Task Title");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(new TaskResponseDto());

        TaskResponseDto response = taskService.getTaskById(taskId);

        assertNotNull(response);
        verify(taskRepository, times(1)).findById(taskId);
    }
}
