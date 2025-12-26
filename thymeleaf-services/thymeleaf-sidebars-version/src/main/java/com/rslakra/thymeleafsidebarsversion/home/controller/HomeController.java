package com.rslakra.thymeleafsidebarsversion.home.controller;

import com.rslakra.thymeleafsidebarsversion.framework.controller.AbstractController;
import com.rslakra.thymeleafsidebarsversion.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/{version}")
public class HomeController extends AbstractController {

    private final HomeService homeService;

    /**
     * @param homeService
     */
    @Autowired
    public HomeController(final HomeService homeService) {
        this.homeService = homeService;
    }

    /**
     * @param version the version path variable
     * @param principal
     * @return
     */
    @GetMapping({"", "/", "index", "home"})
    public String index(@PathVariable("version") String version, Principal principal) {
//        return (Objects.nonNull(principal) ? version + "/home/signedIn" : version + "/home/notSignedIn");
        return version + "/index";
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
    @GetMapping({"/sign-up"})
    public String signUp(@PathVariable("version") String version) {
        return version + "/home/sign-up";
    }

    /**
     * @param version the version path variable
     * @return
     */
    @GetMapping({"/dashboard"})
    public String dashboard(@PathVariable("version") String version) {
        return version + "/dashboard";
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
