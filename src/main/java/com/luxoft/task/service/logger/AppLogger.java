package com.luxoft.task.service.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AppLogger {

    private final Logger logger = LoggerFactory.getLogger("app");

    public void error(String message) {
        logger.error(message);
    }
}
