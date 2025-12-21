package com.rslakra.swaggerservice.service;

import com.rslakra.appsuite.spring.exception.NoRecordFoundException;
import com.rslakra.swaggerservice.persistence.entity.Role;
import com.rslakra.swaggerservice.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @since Aug 08, 2021 15:28:23
 */
@Service
public class RoleService {

    // roleRepository
    private final RoleRepository roleRepository;

    /**
     * @param roleRepository
     */
    @Autowired
    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * @return
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * @param roleId
     * @return
     */
    public Role getRoleById(final Long roleId) {
        return roleRepository.findById(roleId)
            .orElseThrow(() -> new NoRecordFoundException("roleId:%d", roleId));
    }

    /**
     * Returns the role by name.
     *
     * @param name
     * @return
     */
    public Role getByName(String name) {
        Objects.requireNonNull(name);
        return roleRepository.findByName(name)
            .orElseThrow(() -> new NoRecordFoundException("roleName:%s", name));
    }

    /**
     * Returns the list of roles by name.
     *
     * @param name
     * @return
     */
    public List<Role> getRolesByName(String name) {
        Objects.requireNonNull(name);
        return roleRepository.getByName(name);
    }

    /**
     * @param pageable
     * @return
     */
    public Page<Role> getByFilter(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    /**
     * Upsert Role.
     *
     * @param role
     * @return
     */
    public Role upsert(Role role) {
        Objects.requireNonNull(role);
        Role oldRole = role;
        if (!Objects.isNull(role.getId())) {
            oldRole =
                roleRepository.findById(role.getId())
                    .orElseThrow(() -> new NoRecordFoundException("roleId:%d", role.getId()));

            // update role
            oldRole.setName(role.getName());
            oldRole.setStatus(role.getStatus());
        } else {
            // new role
            oldRole = new Role();
            oldRole.setName(role.getName());
            oldRole.setStatus(role.getStatus() != null ? role.getStatus() : com.rslakra.swaggerservice.controller.EntityStatus.ACTIVE);
        }

        // persist role
        oldRole = roleRepository.save(oldRole);
        return oldRole;
    }

    /**
     * Upsert Roles.
     *
     * @param roles
     * @return
     */
    public List<Role> upsert(List<Role> roles) {
        Objects.requireNonNull(roles);
        List<Role> roleList = new ArrayList<>();
        roles.forEach(role -> roleList.add(upsert(role)));
        return roleList;
    }

    /**
     * @param roleId
     */
    public void delete(Long roleId) {
        Objects.requireNonNull(roleId);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new NoRecordFoundException("roleId:%d", roleId));
        roleRepository.delete(role);
    }
}

