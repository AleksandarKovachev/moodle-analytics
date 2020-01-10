package com.moodle.analytics.controller;

import com.moodle.analytics.entity.Configuration;
import com.moodle.analytics.entity.SyncJob;
import com.moodle.analytics.entity.User;
import com.moodle.analytics.error.exception.NotFoundException;
import com.moodle.analytics.form.UserForm;
import com.moodle.analytics.repository.RoleRepository;
import com.moodle.analytics.repository.UserRepository;
import com.moodle.analytics.service.ConfigurationService;
import com.moodle.analytics.service.LogRecordService;
import com.moodle.analytics.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    @Autowired
    private LogRecordService logRecordService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private Validator validator;

    @Autowired
    private MessageSource messageSource;

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

    @GetMapping("/logRecord/error")
    public ModelAndView errorLogRecord() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("logErrorFiles", logRecordService.getLogErrorFiles());
        return new ModelAndView("fragment/errorLogRecords", modelMap);
    }

    @GetMapping("/admin")
    public ModelAndView admin(@ModelAttribute UserForm form) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("form", form);
        modelMap.addAttribute("roles", roleRepository.findAll());
        modelMap.addAttribute("users", userRepository.findAll());
        return new ModelAndView("admin", modelMap);
    }

    @PostMapping("/admin")
    public ModelAndView addUser(@ModelAttribute UserForm form) {
        ModelMap modelMap = new ModelMap();

        List<String> errors = validator
                .validate(form)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        validateRegisteredUserFields(form, errors);

        if (CollectionUtils.isEmpty(errors)) {
            userDetailsService.addUser(form);
            modelMap.addAttribute("message",
                    messageSource.getMessage("successfully.added.user", null, Locale.getDefault()));
            modelMap.addAttribute("form", new UserForm());
        } else {
            modelMap.addAttribute("errors", errors);
            modelMap.addAttribute("form", form);
        }

        modelMap.addAttribute("roles", roleRepository.findAll());
        modelMap.addAttribute("users", userRepository.findAll());
        return new ModelAndView("admin", modelMap);
    }

    @PatchMapping("/user/status/{id}")
    public ResponseEntity<Void> user(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw NotFoundException.builder().entityNotFound().build();
        }
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateRegisteredUserFields(UserForm form, List<String> errors) {
        User user = userRepository.findByUsernameAndEnabled(form.getUsername(), true);
        if (user != null) {
            errors.add(messageSource.getMessage("username.registered", null, Locale.getDefault()));
        }

        user = userRepository.findByEmailAndEnabled(form.getEmail(), true);
        if (user != null) {
            errors.add(messageSource.getMessage("email.registered", null, Locale.getDefault()));
        }
    }

}
