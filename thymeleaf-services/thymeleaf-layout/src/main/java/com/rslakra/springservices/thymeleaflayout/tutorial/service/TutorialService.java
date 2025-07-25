package com.rslakra.springservices.thymeleaflayout.tutorial.service;

import com.rslakra.appsuite.spring.exception.NoRecordFoundException;
import com.rslakra.springservices.thymeleaflayout.framework.service.AbstractService;
import com.rslakra.springservices.thymeleaflayout.tutorial.persistence.entity.Tutorial;
import com.rslakra.springservices.thymeleaflayout.tutorial.persistence.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 1/27/23 12:09 PM
 */
@Service
public class TutorialService extends AbstractService<Tutorial, Long> {
    
    private final TutorialRepository tutorialRepository;
    
    /**
     * @param tutorialRepository
     */
    @Autowired
    public TutorialService(final TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Tutorial findById(final Long id) {
        return tutorialRepository.findById(id).orElseThrow(() -> new NoRecordFoundException("id:%d", id));
    }
    
    /**
     * @return
     */
    @Override
    public List<Tutorial> getAll() {
        return tutorialRepository.findAll();
    }
    
    /**
     * @param tutorial
     * @return
     */
    @Override
    public Tutorial create(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }
    
    /**
     * @param tutorial
     * @return
     */
    @Override
    public Tutorial update(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Tutorial delete(Long id) {
        Tutorial tutorial = findById(id);
        tutorialRepository.deleteById(id);
        return tutorial;
    }
    
    /**
     * @param title
     * @return
     */
    public List<Tutorial> findByTitleContainingIgnoreCase(String title) {
        return tutorialRepository.findByTitleContainingIgnoreCase(title);
    }
    
    /**
     * @param title
     * @param description
     * @return
     */
    public List<Tutorial> findByTitleOrDescriptionContainingIgnoreCase(String title, String description) {
        return tutorialRepository.findByTitleOrDescriptionContainingIgnoreCase(title, description);
    }
    
    /**
     * @param id
     * @param isPublished
     */
    public void updatePublishedStatus(Long id, boolean isPublished) {
        tutorialRepository.updatePublishedStatus(id, isPublished);
    }
}
