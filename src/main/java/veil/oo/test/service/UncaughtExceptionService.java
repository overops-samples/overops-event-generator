package veil.oo.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import veil.oo.test.domain.User;
import veil.oo.test.error.UncaughtException;

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
