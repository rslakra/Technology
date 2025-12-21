package com.rslakra.swaggerservice.controller.rest;

import com.rslakra.swaggerservice.persistence.entity.Role;
import com.rslakra.swaggerservice.payload.PayloadBuilder;
import com.rslakra.swaggerservice.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 8/5/21 11:11 AM
 */
@RestController
@RequestMapping("${apiPrefix}/roles")
@Tag(name = "Role Service")
public class RoleController {

    // @Resource
    private final RoleService roleService;

    /**
     * @param roleService
     */
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * @return
     */
    @GetMapping
    @ResponseBody
    @Operation(summary = "Find all roles", description = "Find all roles",
        tags = {"Role Service"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Find the roles successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class))))
        })
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * @param roleId
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Find role by id", description = "Find role by id",
        tags = {"Role Service"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Find the role successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "No record found")
        })
    public ResponseEntity<Role> getRoleById(
        @Parameter(required = true, description = "Role ID") @PathVariable(value = "id") Long roleId) {
        return ResponseEntity.ok().body(roleService.getRoleById(roleId));
    }

    /**
     * Returns the role by name.
     *
     * @param name
     * @return
     */
    @GetMapping("/byName/{name}")
    @ResponseBody
    public ResponseEntity<Role> getByName(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok().body(roleService.getByName(name));
    }

    /**
     * @param pageable
     * @return
     */
    @GetMapping("/filter")
    public Page<Role> getByFilter(Pageable pageable) {
        return roleService.getByFilter(pageable);
    }

    /**
     * @param role
     * @return
     */
    @PostMapping
    @ResponseBody
    @Operation(summary = "Create new role", description = "Create new role",
        tags = {"Role Service"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Create the role successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)))
        })
    public Role createRole(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create new role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)), required = true)
        @Validated @RequestBody Role role) {
        return roleService.upsert(role);
    }

    /**
     * @param role
     * @return
     */
    @PutMapping
    @ResponseBody
    public ResponseEntity<Role> updateRole(@Validated @RequestBody Role role) {
        return ResponseEntity.ok(roleService.upsert(role));
    }

    /**
     * @param roles
     * @return
     */
    @PostMapping("/batch")
    @ResponseBody
    public List<Role> upsert(@RequestBody List<Role> roles) {
        return roleService.upsert(roles);
    }

    /**
     * @param roleId
     * @return
     */
    @DeleteMapping("/{roleId}")
    @ResponseBody
    public ResponseEntity<PayloadBuilder> delete(@PathVariable(value = "roleId") Long roleId) {
        roleService.delete(roleId);
        return ResponseEntity.ok(PayloadBuilder.newBuilder()
                                     .of("deleted", Boolean.TRUE)
                                     .withMessage("Record with id:" + roleId + " deleted successfully!")
        );
    }

}

