package com.rslakra.springservices.thymeleaflayout.tutorial.persistence.repository;

import com.rslakra.springservices.thymeleaflayout.framework.persistence.repository.AbstractRepository;
import com.rslakra.springservices.thymeleaflayout.tutorial.persistence.entity.Tutorial;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TutorialRepository extends AbstractRepository<Tutorial, Long> {
    
    /**
     * @param title
     * @return
     */
    List<Tutorial> findByTitleContainingIgnoreCase(String title);
    
    
    /**
     * @param title
     * @param description
     * @return
     */
    List<Tutorial> findByTitleOrDescriptionContainingIgnoreCase(String title, String description);
    
    /**
     * @param id
     * @param isPublished
     */
    @Query("UPDATE Tutorial t SET t.published = :isPublished WHERE t.id = :id")
    @Modifying
    public void updatePublishedStatus(Long id, boolean isPublished);
}
