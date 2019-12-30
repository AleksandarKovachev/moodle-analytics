package com.moodle.analytics.controller;

import com.moodle.analytics.entity.Configuration;
import com.moodle.analytics.entity.SyncJob;
import com.moodle.analytics.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Configuration> configure(@PathVariable Long id, @RequestParam String value, Principal principal) {
        Configuration configuration = configurationService.updateConfiguration(id, value, principal);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }

}
