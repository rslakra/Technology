package com.rslakra.thymeleafsidebars.task.persistence.repository;

import com.rslakra.thymeleafsidebars.framework.persistence.repository.AbstractRepository;
import com.rslakra.thymeleafsidebars.task.persistence.entity.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface TaskRepository extends AbstractRepository<Task, Long> {

    /**
     * @param name
     * @return
     */
    List<Task> findByNameContainsIgnoreCase(String name);

    /**
     * @param id
     * @param isCompleted
     */
    @Query("UPDATE Task t SET t.completed = :isCompleted WHERE t.id = :id")
    @Modifying
    public void updateTaskStatus(Long id, boolean isCompleted);
}
