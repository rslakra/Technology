package com.rslakra.springservices.thymeleaflayout.home.service;

import com.rslakra.appsuite.spring.exception.NoRecordFoundException;
import com.rslakra.springservices.thymeleaflayout.framework.service.AbstractService;
import com.rslakra.springservices.thymeleaflayout.home.persistence.entity.Contact;
import com.rslakra.springservices.thymeleaflayout.home.persistence.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 1/27/23 11:43 AM
 */
@Service
public class HomeService extends AbstractService<Contact, Long> {
    
    private final ContactRepository contactRepository;
    
    /**
     * @param contactRepository
     */
    @Autowired
    public HomeService(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Contact findById(final Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new NoRecordFoundException("id:%d", id));
    }
    
    /**
     * @return
     */
    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }
    
    /**
     * @param tutorial
     * @return
     */
    @Override
    public Contact create(Contact tutorial) {
        return contactRepository.save(tutorial);
    }
    
    /**
     * @param tutorial
     * @return
     */
    @Override
    public Contact update(Contact tutorial) {
        return contactRepository.save(tutorial);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Contact delete(Long id) {
        Contact tutorial = findById(id);
        contactRepository.deleteById(id);
        return tutorial;
    }
    
    /**
     * @param firstName
     * @return
     */
    public List<Contact> findByFirstNameContainingIgnoreCase(String firstName) {
        return contactRepository.findByFirstNameContainingIgnoreCase(firstName);
    }
    
    /**
     * @param lastName
     * @return
     */
    public List<Contact> findByLastNameContainingIgnoreCase(String lastName) {
        return contactRepository.findByLastNameContainingIgnoreCase(lastName);
    }
    
}

