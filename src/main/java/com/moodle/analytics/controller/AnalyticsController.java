package com.moodle.analytics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AnalyticsController {

    @GetMapping("/analytics")
    public ModelAndView analytics() {
        return new ModelAndView("analytics");
    }

}
