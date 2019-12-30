package com.moodle.analytics.service;

import com.moodle.analytics.entity.Configuration;
import com.moodle.analytics.error.exception.BadRequestException;
import com.moodle.analytics.error.exception.NotFoundException;
import com.moodle.analytics.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private MessageSource messageSource;

    public List<Configuration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    public Configuration updateConfiguration(Long id, String value, Principal principal) {
        if (!StringUtils.hasText(value)) {
            throw BadRequestException.builder()
                    .detail(messageSource.getMessage("invalid.value", null, Locale.getDefault()))
                    .build();
        }

        Configuration configuration = configurationRepository.findById(id).orElseThrow(() -> NotFoundException.builder().build());
        configuration.setValue(value);
        configuration.setLastModifiedBy(principal.getName());
        return configurationRepository.save(configuration);
    }

}
