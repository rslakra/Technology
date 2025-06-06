package com.rslakra.springservices.thymeleaflayout.home.controller;

import com.rslakra.springservices.thymeleaflayout.framework.controller.AbstractController;
import com.rslakra.springservices.thymeleaflayout.home.entity.Query;
import com.rslakra.springservices.thymeleaflayout.home.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
public class HomeController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private final HomeService homeService;
    // UriComponentsBuilder uriComponentsBuilder;

    /**
     * @param homeService
     */
    @Autowired
    public HomeController(final HomeService homeService) {
        this.homeService = homeService;
    }

    /**
     * @param request
     * @param principal
     * @return
     */
    @GetMapping({"/", "index", "home", "profile"})
    public String index(HttpServletRequest request, Principal principal) {
        LOGGER.debug("+index({}, {}), servletPath={}", request, principal, request.getServletPath());
        String indexPage = "index";
        if ("/home".equals(request.getServletPath())) {
            indexPage = Objects.isNull(principal) ? "home/notSignedIn" : "home/signedIn";
        } else if ("/profile".equals(request.getServletPath())) {
            indexPage = "home/signedIn";
        }

        LOGGER.debug("-index(), indexPage={}", indexPage);
        return indexPage;
    }

    /**
     * @return
     */
    @GetMapping({"/about-us"})
    public String aboutUs() {
        return "home/about-us";
    }

    /**
     * @return
     */
    @GetMapping({"/contact-us"})
    public String contactUs() {
        return "home/contact-us";
    }

    /**
     * @param request
     * @param principal
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    //    @GetMapping({"/login"})
    public String login(HttpServletRequest request, Principal principal, Model model) {
        LOGGER.debug("+login({}, {}), servletPath={}", request, principal, request.getServletPath());
        String loginPage = "home/login";
        if (Objects.nonNull(principal)) {
            loginPage = "index";
        } else if ("get".equals(request.getMethod().toLowerCase())) {
            final List<String> values = Arrays.asList("modal", "dialog");
            String loginType = request.getParameter("loginType");
            if (Objects.nonNull(loginType) && values.contains(loginType)) {
                loginPage = String.format("home/login-%s", loginType);
            }
        } else if ("post".equals(request.getMethod().toLowerCase())) {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            String rememberMe = request.getParameter("_spring_security_remember_me");
            LOGGER.debug("userName={}, password={}, rememberMe={}", userName, password, rememberMe);
            loginPage = "home/signedIn";
        }

        LOGGER.debug("-login(), loginPage={}", loginPage);
        return loginPage;
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public String register(HttpServletRequest request) {
        LOGGER.debug("+register({}), servletPath={}", request, request.getServletPath());
        String signUpPage = "home/register";
        if ("post".equals(request.getMethod().toLowerCase())) {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String dateOfBirth = request.getParameter("dateOfBirth");
            LOGGER.debug("userName={}, password={}, confirmPassword={}, firstName={}, lastName={}, dateOfBirth={}", userName, password, confirmPassword, firstName, lastName, dateOfBirth);
            signUpPage = "home/signedIn";
        }

        LOGGER.debug("-register(), signUpPage={}", signUpPage);
        return signUpPage;
    }

    /**
     * @param request
     * @param principal
     * @return
     */
    @GetMapping({"/logout"})
    public String logout(HttpServletRequest request, Principal principal) {
        LOGGER.debug("+logout({}, {}), servletPath={}", request, principal, request.getServletPath());
        String logoutPage = "index";
        LOGGER.debug("-logout(), logoutPage={}", logoutPage);
        return logoutPage;
    }


    /**
     * @param model
     * @param keyword
     * @return
     */
    @GetMapping(value = "/search")
    public String search(Model model, @Param("keyword") String keyword) {
        LOGGER.debug("+search({})", keyword);
        String searchPage = "home/search";
        List<Query> items = new ArrayList<>();
        if (Objects.nonNull(keyword)) {
            items.add(new Query(keyword, Arrays.asList("First", "Second")));
        }

        model.addAttribute("items", items);

        LOGGER.debug("-search(), keyword={}, model={}", keyword, model);
        return searchPage;
    }

    /**
     * @return
     */
    @GetMapping({"/terms"})
    public String termsOfService() {
        return "terms-of-service";
    }

    /**
     * @return
     */
    @GetMapping({"/privacy-policy"})
    public String privacyPolicy() {
        return "privacy-policy";
    }


}
