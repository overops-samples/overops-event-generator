package com.overops.examples.service;

import com.overops.examples.domain.User;
import com.overops.examples.error.UncaughtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class UncaughtExceptionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void cantCatchMe(User demoUser, boolean generateEvent) {
        Executors.newSingleThreadExecutor().execute(() -> {

            log.trace("user details: {}", demoUser.toString());

            if (generateEvent) {

                log.debug("about to do something very dangerous and unpredictable.");

                /*

                    Uncaught Exception Scenario:

                    This demonstrates what happens when an unexpected and uncaught exception occurs in the code.

                */

                throw new UncaughtException("this exception is uncaught and a BIG potential problem");
            }

            log.debug("no uncaught exception generated this time :)");
        });
    }

}
