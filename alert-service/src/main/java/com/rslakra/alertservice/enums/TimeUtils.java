package com.rslakra.alertservice.enums;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @since 10/06/2024 2:10 PM
 */
public enum TimeUtils {
    INSTANCE;

    long getTimeInSecondsUnixEpoch() {
        return System.currentTimeMillis() / 1000;
    }
}
