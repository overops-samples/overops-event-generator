package veil.oo.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import veil.oo.test.domain.User;
import veil.oo.test.utils.TakipiAverageExecutionTime;

@Service
public class LoggedWarnService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @TakipiAverageExecutionTime
    public void warningsAbound(User demoUser, boolean generateEvent) {

        log.trace("user details: {}", demoUser.toString());

        if (generateEvent) {

             /*

                Warn Scenario:

                OverOps captures detailed snapshots when calls to log.warn are made.

             */

            log.warn("log.warn() called!");
        }
    }
}
