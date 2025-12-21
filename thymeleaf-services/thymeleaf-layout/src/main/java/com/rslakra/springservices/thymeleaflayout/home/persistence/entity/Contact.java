package com.rslakra.springservices.thymeleaflayout.home.persistence.entity;

import com.rslakra.appsuite.core.ToString;
import com.rslakra.springservices.thymeleaflayout.framework.persistence.entity.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contact extends AbstractEntity {
    
    @Column(length = 64, nullable = false)
    private String firstName;
    
    @Column(length = 64, nullable = false)
    private String lastName;
    
    @Column(length = 32, nullable = false)
    private String country;
    
    @Column(length = 256, nullable = false)
    private String subject;
    
    public Contact() {
    }
    
    /**
     * @param firstName
     * @param lastName
     * @param country
     * @param subject
     */
    public Contact(String firstName, String lastName, String country, String subject) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.subject = subject;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /**
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(Contact.class, true)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("country", country)
                .add("subject", subject)
                .toString();
    }
    
}
