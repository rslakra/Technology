package com.rslakra.thymeleafsidebarsversion.report.controller;

import com.rslakra.thymeleafsidebarsversion.framework.controller.AbstractController;
import com.rslakra.thymeleafsidebarsversion.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/{version}/reports")
public class ReportController extends AbstractController {

    private final ReportService reportService;

    /**
     * @param reportService
     */
    @Autowired
    public ReportController(final ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * @param principal
     * @return
     */
    /**
     * @param version the version path variable
     * @param principal
     * @return
     */
    @GetMapping
    public String reportIndex(@PathVariable("version") String version, Principal principal) {
//        return (Objects.nonNull(principal) ? version + "/home/signedIn" : version + "/home/notSignedIn");
        return version + "/report/index";
    }

    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/about-us"})
    public String aboutUs(@PathVariable("version") String version) {
        return version + "/home/about-us";
    }

    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/contact-us"})
    public String contactUs(@PathVariable("version") String version) {
        return version + "/home/contact-us";
    }

    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/sign-in"})
    public String signedIn(@PathVariable("version") String version) {
        return version + "/home/signedIn";
    }

    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/profile"})
    public String profile(@PathVariable("version") String version) {
        return "redirect:/" + version;
    }

    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/dashboard"})
    public String dashboard(@PathVariable("version") String version) {
        return "redirect:/" + version;
    }


    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/settings"})
    public String settings(@PathVariable("version") String version) {
        return "redirect:/" + version + "/settings";
    }


    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/logout"})
    public String logout(@PathVariable("version") String version) {
        return "redirect:/" + version;
    }
}
