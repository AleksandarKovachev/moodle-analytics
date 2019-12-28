package com.moodle.analytics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @GetMapping("/configure")
    public ModelAndView configure() {
        return new ModelAndView("configure");
    }

}
