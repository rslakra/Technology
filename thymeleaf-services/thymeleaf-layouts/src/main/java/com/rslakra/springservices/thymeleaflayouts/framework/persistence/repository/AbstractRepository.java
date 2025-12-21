package com.rslakra.springservices.thymeleaflayouts.framework.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.transaction.Transactional;

@NoRepositoryBean
@Transactional
public interface AbstractRepository<T, ID> extends JpaRepository<T, ID> {

}
