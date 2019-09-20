package com.overops.examples.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.overops.examples.domain.User;

@Service
public class SlowService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void longRunningMethod(User demoUser, boolean generateEvent) {

        log.trace("user details: {}", demoUser.toString());

        if (generateEvent) {
            log.info("i'm concerned this method could take too long to execute...");

             /*

                Timer Scenario:

                OverOps can be configured to capture events when method duration exceeds a user defined threshold

             */

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
