package com.rslakra.thymeleaftemplates.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 8/21/21 5:46 AM
 */
@Controller
@RequestMapping("/v0")
public class HomeControllerV0 {

    /**
     * Home Page
     *
     * @return
     */
    @GetMapping
    public String homePage() {
        return "views/v0/home";
    }

    @GetMapping("about-us")
    public String aboutUsPage() {
        return "views/v0/about-us";
    }

    @GetMapping("contact-us")
    public String contactUsPage() {
        return "views/v0/contact-us";
    }

    @GetMapping("admin")
    public String adminPage() {
        return "views/v0/admin/index";
    }

    @GetMapping("report")
    public String reportPage() {
        return "views/v0/report/index";
    }

    @GetMapping("setting")
    public String settingPage() {
        return "views/v0/setting/index";
    }

    @GetMapping("tasks")
    public String taskPage(Model model) {
        // Initialize empty tasks list to prevent null pointer exceptions in template
        List<?> tasks = new ArrayList<>();
        model.addAttribute("tasks", tasks);
        return "views/v0/task/listTasks";
    }

    @GetMapping("roles")
    public String rolesListPage() {
        return "views/v0/account/role/listRoles";
    }

    @GetMapping("users")
    public String usersListPage() {
        return "views/v0/account/user/listUsers";
    }

    @GetMapping("artists")
    public String artistsListPage() {
        return "views/v0/artist/listArtists";
    }

    @GetMapping("songs")
    public String songsListPage() {
        return "views/v0/song/listSongs";
    }

}
