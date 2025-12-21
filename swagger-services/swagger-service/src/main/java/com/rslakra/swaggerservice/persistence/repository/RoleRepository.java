package com.rslakra.swaggerservice.persistence.repository;

import com.rslakra.swaggerservice.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @since Aug 08, 2021 15:26:40
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * @param name
     * @return
     */
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(@Param("name") String name);

    /**
     * Returns the list of roles by name.
     *
     * @param name
     * @return
     */
    List<Role> getByName(String name);

}

