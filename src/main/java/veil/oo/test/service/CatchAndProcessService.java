package veil.oo.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import veil.oo.test.domain.User;
import veil.oo.test.error.BusinessException;

@Service
public class CatchAndProcessService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void handleException(User demoUser, boolean generateEvent) {

        log.trace("user details: {}", demoUser.toString());

        boolean exceptionOccurred = false;

        try {

            if (generateEvent) {

                throw new BusinessException("this exception is thrown in one method and expected to be handled in another.");

            }

        } catch (BusinessException e) {
            log.debug("here we catch: " + e.getMessage(), e);

            exceptionOccurred = true;

        }

        log.debug("an exception occurred in this method.  value = {}", exceptionOccurred);
    }


}
