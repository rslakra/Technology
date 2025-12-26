package com.rslakra.thymeleafsidebarsversion.performance.controller;

import com.rslakra.thymeleafsidebarsversion.framework.controller.AbstractController;
import com.rslakra.thymeleafsidebarsversion.performance.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/{version}/performance")
public class PerformanceController extends AbstractController {

    private final PerformanceService performanceService;

    /**
     * @param performanceService
     */
    @Autowired
    public PerformanceController(final PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    /**
     * @param version the version path variable
     * @param principal
     * @return
     */
    @GetMapping
    public String performanceIndex(@PathVariable("version") String version, Principal principal) {
        return version + "/performance/index";
    }

}
