package com.rslakra.technology.chatservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home controller to handle root path requests.
 * 
 * @author Rohtash Lakra
 */
@Controller
public class HomeController {

    /**
     * Handles /chatting-service path and redirects to root.
     * The root path (/) will automatically serve index.html from static resources.
     * 
     * @return redirect to root
     */
    @GetMapping("/chatting-service")
    public String redirectToRoot() {
        return "redirect:/";
    }
}

