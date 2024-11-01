package com.pr.tm.util;

import com.pr.tm.dto.TaskCreateRequestDto;
import com.pr.tm.model.TaskStatus;
import com.pr.tm.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DataInit implements ApplicationRunner {
    private final TaskService taskService;

    @Autowired
    public DataInit(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (taskService.getAllTasks().isEmpty()) {
            log.info("No tasks found. Adding some tasks...");

            for (int i = 1; i <= 9; i++) {
                TaskCreateRequestDto dto = new TaskCreateRequestDto();
                dto.setTitle("Task " + i);
                dto.setDescription("Task " + i + " description");
                if (i % 2 == 0) {
                    dto.setStatus(TaskStatus.IN_PROGRESS);
                } else if (i % 3 == 0) {
                    dto.setStatus(TaskStatus.COMPLETED);
                }
                taskService.create(dto);
            }
        }
    }
}
