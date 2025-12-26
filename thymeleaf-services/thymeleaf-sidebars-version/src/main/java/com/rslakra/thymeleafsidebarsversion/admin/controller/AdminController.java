package com.rslakra.thymeleafsidebarsversion.admin.controller;

import com.rslakra.thymeleafsidebarsversion.admin.service.AdminService;
import com.rslakra.thymeleafsidebarsversion.framework.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/{version}/admin")
public class AdminController extends AbstractController {

    private final AdminService adminService;

    /**
     * @param adminService
     */
    @Autowired
    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * @param version the version path variable
     * @param principal
     * @return
     */
    @GetMapping
    public String adminIndex(@PathVariable("version") String version, Principal principal) {
//        return (Objects.nonNull(principal) ? version + "/home/signedIn" : version + "/home/notSignedIn");
        return version + "/admin/index";
    }

}
