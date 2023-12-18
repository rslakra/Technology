package com.rslakra.springservices.thymeleaflayouts.home.controller;

import com.rslakra.springservices.thymeleaflayouts.framework.controller.AbstractController;
import com.rslakra.springservices.thymeleaflayouts.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
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
     * @param principal
     * @return
     */
    @GetMapping({"/", "index", "home"})
    public String homePage(Principal principal) {
//        return (Objects.nonNull(principal) ? "contact-us/signedIn" : "contact-us/notSignedIn");
        return "index";
    }

    /**
     * @return
     */
    @GetMapping({"/about-us"})
    public String aboutUs() {
        return "views/about-us";
    }

    /**
     * @return
     */
    @GetMapping({"/contact-us"})
    public String contactUs() {
        return "views/contact-us";
    }

    /**
     * @return
     */
    @GetMapping({"/sign-in"})
    public String signedIn() {
        return "views/signedIn";
    }

    /**
     * @return
     */
    @GetMapping({"/logout"})
    public String logout() {
        return "index";
    }
}
