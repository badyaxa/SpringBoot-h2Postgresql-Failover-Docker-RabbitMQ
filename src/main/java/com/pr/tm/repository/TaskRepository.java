package com.pr.tm.repository;

import com.pr.tm.model.Task;
import com.pr.tm.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTitleAndStatusNot(String title, TaskStatus status);

    Optional<Task> findByIdAndStatusNot(Long id, TaskStatus status);

    Long countByStatusNot(TaskStatus status);
}
