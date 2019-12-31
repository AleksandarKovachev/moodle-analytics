package com.moodle.analytics.service;

import com.moodle.analytics.config.SpringQuartzScheduler;
import com.moodle.analytics.entity.Configuration;
import com.moodle.analytics.error.exception.BadRequestException;
import com.moodle.analytics.error.exception.NotFoundException;
import com.moodle.analytics.repository.ConfigurationRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Service
@Slf4j
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SchedulerFactoryBean scheduler;

    @Autowired
    private JobDetail job;

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

        if (id.equals(1L)) {
            validateLogDirectoryPath(value);
        }

        Configuration configuration = configurationRepository.findById(id).orElseThrow(() -> NotFoundException.builder().build());
        configuration.setValue(value);
        configuration.setLastModifiedBy(principal.getName());

        if (id.equals(2L)) {
            rescheduleJob(value);
        }

        return configurationRepository.save(configuration);
    }

    private void validateLogDirectoryPath(String value) {
        try {
            Path path = Paths.get(value);
            File file = path.toFile();
            if (!file.exists() || !file.isDirectory()) {
                throw BadRequestException.builder()
                        .detail(messageSource.getMessage("invalid.value", null, Locale.getDefault()))
                        .build();
            }
        } catch (InvalidPathException ex) {
            throw BadRequestException.builder()
                    .detail(messageSource.getMessage("invalid.value", null, Locale.getDefault()))
                    .build();
        }
    }

    private void rescheduleJob(String value) {
        try {
            scheduler.getScheduler().unscheduleJob(new TriggerKey(SpringQuartzScheduler.TRIGGER_KEY));
            scheduler.getScheduler().scheduleJob(TriggerBuilder.newTrigger()
                    .forJob(job)
                    .withIdentity(SpringQuartzScheduler.TRIGGER_KEY)
                    .withSchedule(simpleSchedule().repeatForever().withIntervalInMilliseconds(Long.parseLong(value)))
                    .build());
        } catch (SchedulerException e) {
            log.error("An error has occurred while rescheduling the job", e);
        }
    }

}
