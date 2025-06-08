package com.rslakra.springservices.thymeleaflayout.task.persistence.repository;

import com.rslakra.springservices.thymeleaflayout.framework.persistence.repository.AbstractRepository;
import com.rslakra.springservices.thymeleaflayout.task.persistence.entity.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TaskRepository extends AbstractRepository<Task, Long> {
    
    /**
     * @param name
     * @return
     */
    List<Task> findByNameContainsIgnoreCase(String name);
    
    /**
     * @param name
     * @param description
     * @return
     */
    List<Task> findByNameOrDescriptionContainsIgnoreCase(String name, String description);
    
    /**
     * @param id
     * @param isCompleted
     */
    @Query("UPDATE Task t SET t.completed = :isCompleted WHERE t.id = :id")
    @Modifying
    public void updateTaskStatus(Long id, boolean isCompleted);
}
