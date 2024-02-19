package com.luxoft.task.service.logger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public final class AppLogger {

    private final Logger logger = getLogger("app");

    public void error(String message) {
        logger.error(message);
    }
}
