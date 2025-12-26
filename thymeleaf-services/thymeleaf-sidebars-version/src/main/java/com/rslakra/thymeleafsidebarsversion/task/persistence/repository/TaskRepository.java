package com.rslakra.thymeleafsidebarsversion.task.persistence.repository;

import com.rslakra.thymeleafsidebarsversion.framework.persistence.repository.AbstractRepository;
import com.rslakra.thymeleafsidebarsversion.task.persistence.entity.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
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
     * @param type
     * @return
     */
    List<Task> findByType(String type);

    /**
     * @param type
     * @param name
     * @return
     */
    List<Task> findByTypeAndNameContainsIgnoreCase(String type, String name);

    /**
     * @param id
     * @param isCompleted
     */
    @Query("UPDATE Task t SET t.completed = :isCompleted WHERE t.id = :id")
    @Modifying
    public void updateTaskStatus(Long id, boolean isCompleted);
}
