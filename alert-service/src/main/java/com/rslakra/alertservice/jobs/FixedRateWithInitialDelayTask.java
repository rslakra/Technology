package com.rslakra.alertservice.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Rohtash Lakra
 * @created 12/5/22 22:31 PM
 */
public class FixedRateWithInitialDelayTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixedRateWithInitialDelayTask.class);

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long timeInSecondsUnixEpoch = System.currentTimeMillis() / 1000;
        LOGGER.debug("Fixed rate task with one second initial delay - {timeInSecondsUnixEpoch}", timeInSecondsUnixEpoch);
    }
}
