package com.moodle.analytics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    private ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    private ModelAndView login() {
        return new ModelAndView("login");
    }

}
