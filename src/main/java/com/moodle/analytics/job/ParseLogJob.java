package com.moodle.analytics.job;

import com.moodle.analytics.service.ParseLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Parse Log Job
 * 
 * @author aleksandar.kovachev
 */
@Slf4j
@Component
public class ParseLogJob implements Job {

	@Autowired
	private ParseLogService parseLogService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("ParseLogJob has been fired @ {}", context.getFireTime());
		parseLogService.parseLogs();
		log.info("ParseLogJob next job scheduled @ {}", context.getNextFireTime());
	}

}
