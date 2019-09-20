package com.overops.examples.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.overops.examples.domain.User;
import com.overops.examples.error.BusinessException;

import io.sentry.overops.examples.utils.SentryUtil;

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
            SentryUtil.capture(e);
        }

        log.debug("an exception occurred in this method.  value = {}", exceptionOccurred);
    }


}
