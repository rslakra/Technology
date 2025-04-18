package com.rslakra.alertservice.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * By default, Spring will use the server's local time zone for the cron expression. However, we can use the zone
 * attribute to change this timezone:
 *
 * <pre>
 *  @Scheduled(cron = "0 15 10 15 * ?", zone = "Europe/Paris")
 * </pre>
 * <p>
 * With this configuration, Spring will schedule the annotated method to run at 10:15 AM on the 15th day of every month
 * in Paris time.
 *
 * @author Rohtash Lakra
 * @created 12/5/22 22:32 PM
 */
public class TaskUsingCronExpression {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskUsingCronExpression.class);

    @Scheduled(cron = "0 15 10 15 * ?")
    public void scheduleTaskUsingCronExpression() {
        long timeInSecondsUnixEpoch = System.currentTimeMillis() / 1000;
        LOGGER.debug("schedule tasks using cron jobs - {timeInSecondsUnixEpoch}", timeInSecondsUnixEpoch);
    }
}
