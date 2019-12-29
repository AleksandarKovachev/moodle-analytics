package com.moodle.analytics.service;

import com.moodle.analytics.entity.Configuration;
import com.moodle.analytics.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public List<Configuration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

}
