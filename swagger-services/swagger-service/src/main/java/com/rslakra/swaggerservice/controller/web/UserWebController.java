package com.rslakra.swaggerservice.controller.web;

import com.rslakra.swaggerservice.persistence.entity.Role;
import com.rslakra.swaggerservice.persistence.entity.User;
import com.rslakra.swaggerservice.service.RoleService;
import com.rslakra.swaggerservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author: Rohtash Lakra
  * @since 09/30/2019 05:38 PM
 */
@Controller
@RequestMapping("/users")
public class UserWebController {

    // userService
    private final UserService userService;
    // roleService
    private final RoleService roleService;

    /**
     * @param userService
     * @param roleService
     */
    @Autowired
    public UserWebController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping("/save")
    public String saveUser(User user, @RequestParam(required = false) List<Long> roleIds) {
        // Handle role IDs from form submission
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : roleIds) {
                if (roleId != null) {
                    Role role = roleService.getRoleById(roleId);
                    roles.add(role);
                }
            }
            user.setRoles(roles);
        } else {
            // Clear roles if none selected
            user.setRoles(new HashSet<>());
        }
        user = userService.upsert(user);
        return "redirect:/users/list";
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/listUsers";
    }

    /**
     * @param model
     * @param userId
     * @return
     */
    @GetMapping(path = {"/create", "/update/{userId}"})
    public String upsertUser(Model model, @PathVariable(name = "userId") Optional<Long> userId) {
        User user = null;
        if (userId.isPresent()) {
            user = userService.getUserById(userId.get());
        } else {
            user = new User();
        }
        model.addAttribute("user", user);
        
        // Add all roles for selection
        List<Role> allRoles = roleService.getAllRoles();
        model.addAttribute("allRoles", allRoles);

        return "user/editUser";
    }

    /**
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping("/delete/{userId}")
    public String delete(Model model, @PathVariable(name = "userId") Long userId) {
        userService.delete(userId);
        return "redirect:/users/list";
    }

}
