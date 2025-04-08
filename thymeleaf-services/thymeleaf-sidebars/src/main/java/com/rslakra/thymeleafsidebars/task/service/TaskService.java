package com.rslakra.thymeleafsidebars.task.service;

import com.rslakra.thymeleafsidebars.framework.service.AbstractService;
import com.rslakra.thymeleafsidebars.task.persistence.entity.Task;

import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 1/27/23 11:43 AM
 */
public interface TaskService extends AbstractService<Task> {

    public List<Task> findByNameContainsIgnoreCase(String name);

    /**
     * @param id
     * @param isCompleted
     */
    public void updateTaskStatus(Long id, boolean isCompleted);
}
