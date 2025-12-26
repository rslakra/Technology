package com.rslakra.thymeleafsidebarsversion.development.controller;

import com.rslakra.thymeleafsidebarsversion.framework.controller.AbstractController;
import com.rslakra.thymeleafsidebarsversion.development.service.DevelopmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/{version}/development")
public class DevelopmentController extends AbstractController {

    private final DevelopmentService developmentService;

    /**
     * @param developmentService
     */
    @Autowired
    public DevelopmentController(final DevelopmentService developmentService) {
        this.developmentService = developmentService;
    }

    /**
     * @param version the version path variable
     * @param principal
     * @return
     */
    @GetMapping
    public String developmentIndex(@PathVariable("version") String version, Principal principal) {
        return version + "/development/index";
    }

}
