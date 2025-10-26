package com.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling home page requests
 */
@Controller
public class HomeController {

    /**
     * Display the home page
     * @return View name
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
}