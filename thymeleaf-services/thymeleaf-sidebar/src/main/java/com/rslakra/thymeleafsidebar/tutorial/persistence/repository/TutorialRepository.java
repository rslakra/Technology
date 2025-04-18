package com.rslakra.thymeleafsidebar.tutorial.persistence.repository;

import com.rslakra.thymeleafsidebar.framework.persistence.repository.AbstractRepository;
import com.rslakra.thymeleafsidebar.tutorial.persistence.entity.Tutorial;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TutorialRepository extends AbstractRepository<Tutorial, Long> {

    /**
     * @param keyword
     * @return
     */
    List<Tutorial> findByTitleContainingIgnoreCase(String keyword);

    /**
     * @param id
     * @param isPublished
     */
    @Query("UPDATE Tutorial t SET t.published = :isPublished WHERE t.id = :id")
    @Modifying
    public void updatePublishedStatus(Long id, boolean isPublished);
}
