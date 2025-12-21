package com.rslakra.swaggerservice.controller.web;

import com.rslakra.swaggerservice.persistence.entity.Role;
import com.rslakra.swaggerservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/**
 * @author: Rohtash Lakra
 * @since 09/30/2019 05:38 PM
 */
@Controller
@RequestMapping("/roles")
public class RoleWebController {

    // roleService
    private final RoleService roleService;

    /**
     * @param roleService
     */
    @Autowired
    public RoleWebController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * @param role
     * @return
     */
    @PostMapping("/save")
    public String saveRole(Role role) {
        role = roleService.upsert(role);
        return "redirect:/roles/list";
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String getRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "role/listRoles";
    }

    /**
     * @param model
     * @param roleId
     * @return
     */
    @GetMapping(path = {"/create", "/update/{roleId}"})
    public String upsertRole(Model model, @PathVariable(name = "roleId") Optional<Long> roleId) {
        Role role = null;
        if (roleId.isPresent()) {
            role = roleService.getRoleById(roleId.get());
        } else {
            role = new Role();
        }
        model.addAttribute("role", role);
        return "role/editRole";
    }

    /**
     * @param model
     * @param roleId
     * @return
     */
    @RequestMapping("/delete/{roleId}")
    public String delete(Model model, @PathVariable(name = "roleId") Long roleId) {
        roleService.delete(roleId);
        return "redirect:/roles/list";
    }

}

