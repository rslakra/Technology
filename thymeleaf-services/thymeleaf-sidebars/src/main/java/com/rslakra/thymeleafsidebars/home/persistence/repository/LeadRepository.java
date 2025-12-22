package com.rslakra.thymeleafsidebars.home.persistence.repository;

import com.rslakra.thymeleafsidebars.framework.persistence.repository.AbstractRepository;
import com.rslakra.thymeleafsidebars.home.persistence.entity.Lead;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface LeadRepository extends AbstractRepository<Lead, Long> {

    /**
     * @param email
     * @return
     */
    Optional<Lead> findByEmail(String email);
}
