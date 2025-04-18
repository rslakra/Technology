package com.rslakra.auditservice.persistence.repository;

import com.rslakra.auditservice.persistence.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rohtash Lakra
 * @created 8/4/21 5:57 PM
 */
public interface FileRepository extends JpaRepository<File, Long> {

}
