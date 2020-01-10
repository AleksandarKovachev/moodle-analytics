package com.moodle.analytics.controller;

import com.moodle.analytics.entity.User;
import com.moodle.analytics.form.ChangePasswordForm;
import com.moodle.analytics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class ChangePasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Validator validator;

    @GetMapping("/changePassword")
    public ModelAndView changePassword(@ModelAttribute ChangePasswordForm form) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("form", form);
        return new ModelAndView("changePassword", modelMap);
    }

    @PostMapping("/changePassword")
    public ModelAndView changePasswordPost(@ModelAttribute ChangePasswordForm form, Principal principal) {
        ModelMap modelMap = new ModelMap();

        List<String> errors = validator
                .validate(form)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(errors) && !form.getNewPassword().equals(form.getRepeatNewPassword())) {
            errors.add(messageSource.getMessage("repeat.password.wrong", null, Locale.getDefault()));
        }
        if (CollectionUtils.isEmpty(errors)) {
            User user = validateOldPassword(form, principal, errors);
            if (CollectionUtils.isEmpty(errors)) {
                saveChangedPassword(form, modelMap, user);
            }
        }
        if (!CollectionUtils.isEmpty(errors)) {
            modelMap.addAttribute("errors", errors);
        }
        modelMap.addAttribute("form", form);
        return new ModelAndView("changePassword", modelMap);
    }

    private User validateOldPassword(@ModelAttribute ChangePasswordForm form, Principal principal, List<String> errors) {
        User user = userRepository.findByUsernameAndEnabled(principal.getName(), true);
        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            errors.add(messageSource.getMessage("old.password.wrong", null, Locale.getDefault()));
        }
        return user;
    }

    private void saveChangedPassword(@ModelAttribute ChangePasswordForm form, ModelMap modelMap, User user) {
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
        modelMap.addAttribute("message",
                messageSource.getMessage("successfully.changed.password", null, Locale.getDefault()));
    }

}
