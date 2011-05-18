package com.github.fallback.util;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * Simple runtime manager of our log4j log levels; config'd for jmx access.
 */
@Component
@ManagedResource
public class LogLevelManager {

    @ManagedOperation
    public void setLoggerLevel(String category, String level) {
        Logger.getLogger(category).setLevel(Level.toLevel(level));
    }

    @ManagedOperation
    public String getLoggerLevel(String category) {
        Category logger = Logger.getLogger(category);
        Level logLevel = logger.getLevel();

        if (logLevel == null) {
            while (logLevel == null) {
                logger = logger.getParent();
                logLevel = logger.getLevel();
            }
        }

        return logLevel.toString();
    }

    @ManagedOperation
    public void setDebug() {
        setLoggerLevel("com.github.fallback", "debug");
    }

    @ManagedOperation
    public void setInfo() {
        setLoggerLevel("com.github.fallback", "info");
    }
}
