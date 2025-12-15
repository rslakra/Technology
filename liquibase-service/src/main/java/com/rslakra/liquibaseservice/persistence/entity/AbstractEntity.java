package com.rslakra.liquibaseservice.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * @author Rohtash Lakra
 * @created 10/20/22 12:03 PM
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class AbstractEntity<U> extends Auditable<U> {

    @Id
    @GeneratedValue
    private Long id;

}
