package com.moodle.analytics.service;

import com.moodle.analytics.entity.Role;
import com.moodle.analytics.entity.User;
import com.moodle.analytics.form.UserForm;
import com.moodle.analytics.repository.RoleRepository;
import com.moodle.analytics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public
class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException(username);
        }
        return userRepository.findByUsernameAndEnabled(username, true);
    }

    public UserDetails addUser(UserForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCreatedTimestamp(new Date());
        user.setUpdatedTimestamp(new Date());

        Role role = roleRepository.findById(form.getRoleId()).orElseThrow(IllegalArgumentException::new);
        user.setRole(role);
        user = userRepository.save(user);
        return user;
    }

}