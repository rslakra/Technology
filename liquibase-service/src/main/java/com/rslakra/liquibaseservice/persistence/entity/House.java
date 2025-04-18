package com.rslakra.liquibaseservice.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author Rohtash Lakra
 */
@Getter
@Setter
@Entity(name = "houses")
public class House extends AbstractEntity<String> {

    private String owner;
    private boolean fullyPaid;
}
