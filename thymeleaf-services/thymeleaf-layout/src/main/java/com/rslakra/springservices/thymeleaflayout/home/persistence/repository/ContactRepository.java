package com.rslakra.springservices.thymeleaflayout.home.persistence.repository;

import com.rslakra.springservices.thymeleaflayout.framework.persistence.repository.AbstractRepository;
import com.rslakra.springservices.thymeleaflayout.home.persistence.entity.Contact;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ContactRepository extends AbstractRepository<Contact, Long> {
    
    /**
     * @param firstName
     * @return
     */
    List<Contact> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * @param lastName
     * @return
     */
    List<Contact> findByLastNameContainingIgnoreCase(String lastName);
    
}
