package com.rslakra.swaggerservice.persistence.entity;

import com.rslakra.swaggerservice.controller.EntityStatus;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * @author Rohtash Lakra
 * @created 8/20/21 7:11 PM
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends NamedEntity {

    @Column(name = "status", length = 8, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EntityStatus status = EntityStatus.INACTIVE;

}
