package com.rslakra.thymeleafsidebars.tutorial.service;

import com.rslakra.thymeleafsidebars.framework.service.AbstractService;
import com.rslakra.thymeleafsidebars.tutorial.persistence.entity.Tutorial;

import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 1/27/23 12:09 PM
 */
public interface TutorialService extends AbstractService<Tutorial> {

    /**
     * @param keyword
     * @return
     */
    public List<Tutorial> findByTitleContainingIgnoreCase(String keyword);

    /**
     * @param id
     * @param isPublished
     */
    public void updatePublishedStatus(Long id, boolean isPublished);
}
