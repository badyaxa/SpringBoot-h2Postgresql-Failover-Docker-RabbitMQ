package com.pr.tm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr.tm.dto.TaskCreateRequestDto;
import com.pr.tm.dto.TaskResponseDto;
import com.pr.tm.dto.TaskUpdateRequestDto;
import com.pr.tm.exception.NotFoundException;
import com.pr.tm.model.TaskStatus;
import com.pr.tm.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void createTask_success() throws Exception {
        TaskCreateRequestDto dto = new TaskCreateRequestDto();
        dto.setTitle("Task 1");
        dto.setDescription("Task 1 description");
        when(taskService.create(any(TaskCreateRequestDto.class))).thenReturn(1L);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(taskService, times(1)).create(any(TaskCreateRequestDto.class));
    }

    @Test
    void createTask_invalidInput() throws Exception {
        TaskCreateRequestDto dto = new TaskCreateRequestDto();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(taskService, times(0)).create(any(TaskCreateRequestDto.class));
    }

    @Test
    void deleteTask_success() throws Exception {
        Long taskId = 1L;

        doNothing().when(taskService).delete(taskId);

        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).delete(taskId);
    }

    @Test
    void deleteTask_notFound() throws Exception {
        Long taskId = 1L;

        doThrow(new NotFoundException("Task not found")).when(taskService).delete(taskId);

        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).delete(taskId);
    }

    @Test
    void updateTask_success() throws Exception {
        Long taskId = 1L;
        TaskUpdateRequestDto requestDto = new TaskUpdateRequestDto();
        requestDto.setTitle("Updated Task Title");
        requestDto.setDescription("Updated Task Description");
        requestDto.setStatus(TaskStatus.IN_PROGRESS);

        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(taskId);
        responseDto.setTitle("Updated Task Title");
        responseDto.setDescription("Updated Task Description");
        responseDto.setStatus(TaskStatus.IN_PROGRESS);

        when(taskService.update(eq(taskId), any(TaskUpdateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Updated Task Title"))
                .andExpect(jsonPath("$.description").value("Updated Task Description"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(taskService, times(1)).update(eq(taskId), any(TaskUpdateRequestDto.class));
    }

    @Test
    void updateTask_invalidInput() throws Exception {
        Long taskId = 1L;
        TaskUpdateRequestDto dto = new TaskUpdateRequestDto();

        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(taskService, times(0)).update(anyLong(), any(TaskUpdateRequestDto.class));
    }

//    @Test // TODO: Enable this test after implementing the test
//    void updateTask_notFound() { // TODO: Implement this test
//    }

//    @Test // TODO: Enable this test after implementing the test
//    void patchTask_success() { // TODO: Implement this test
//    }

//    @Test // TODO: Enable this test after implementing the test
//    void patchTask_invalidInput() { // TODO: Implement this test
//    }

//    @Test // TODO: Enable this test after implementing the test
//    void patchTask_notFound() { // TODO: Implement this test
//    }

    @Test
    void getAllTasks_success() throws Exception {
        List<TaskResponseDto> tasks = new ArrayList<>();
        TaskResponseDto task1 = new TaskResponseDto();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        tasks.add(task1);

        TaskResponseDto task2 = new TaskResponseDto();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        tasks.add(task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].status").value("NEW"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].status").value("IN_PROGRESS"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_success() throws Exception {
        Long taskId = 1L;
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(taskId);
        responseDto.setTitle("Task 1");
        responseDto.setDescription("Description 1");
        responseDto.setStatus(TaskStatus.NEW);

        when(taskService.getTaskById(taskId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.status").value("NEW"));

        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void getTaskById_notFound() throws Exception {
        Long taskId = 1L;

        when(taskService.getTaskById(taskId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void inProgressTask_success() throws Exception {
        Long taskId = 1L;
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(taskId);
        responseDto.setTitle("Task 1");
        responseDto.setDescription("Description 1");
        responseDto.setStatus(TaskStatus.IN_PROGRESS);

        when(taskService.inProgress(taskId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/tasks/{id}/in-progress", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(taskService, times(1)).inProgress(taskId);
    }

    @Test
    void inProgressTask_notFound() throws Exception {
        Long taskId = 1L;

        when(taskService.inProgress(taskId)).thenThrow(new NotFoundException("Task not found"));

        mockMvc.perform(get("/api/v1/tasks/{id}/in-progress", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Object not found: Task not found"));
        verify(taskService, times(1)).inProgress(taskId);
    }

    @Test
    void completeTask_success() throws Exception {
        Long taskId = 1L;
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(taskId);
        responseDto.setTitle("Task 1");
        responseDto.setDescription("Description 1");
        responseDto.setStatus(TaskStatus.COMPLETED);

        when(taskService.complete(taskId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/tasks/{id}/complete", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(taskService, times(1)).complete(taskId);
    }

    @Test
    void completeTask_notFound() throws Exception {
        Long taskId = 1L;

        when(taskService.complete(taskId)).thenThrow(new NotFoundException("Task not found"));

        mockMvc.perform(get("/api/v1/tasks/{id}/complete", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Object not found: Task not found"));

        verify(taskService, times(1)).complete(taskId);
    }
}
