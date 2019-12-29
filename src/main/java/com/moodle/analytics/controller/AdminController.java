package com.moodle.analytics.controller;

import com.moodle.analytics.entity.SyncJob;
import com.moodle.analytics.repository.ConfigurationRepository;
import com.moodle.analytics.service.ConfigurationService;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class AdminController {

    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/configure")
    public ModelAndView configure() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("configurations", configurationService.getAllConfigurations());
        modelMap.addAttribute("syncJobValues", SyncJob.values());
        return new ModelAndView("configure", modelMap);
    }

    @PostMapping("/configure/{id}")
    public ModelAndView configure(@PathVariable Long id, @RequestParam String value, Principal principal) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("configurations", configurationService.getAllConfigurations());
        modelMap.addAttribute("syncJobValues", SyncJob.values());
        return new ModelAndView("configure", modelMap);
    }

}
