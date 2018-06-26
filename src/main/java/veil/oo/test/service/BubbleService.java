package veil.oo.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import veil.oo.test.domain.User;
import veil.oo.test.error.BusinessException;

@Service
public class BubbleService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void bubbleException(User demoUser, boolean generateEvent) throws BusinessException {

        log.trace("user details: {}", demoUser.toString());

        if (generateEvent) {

            /*

                Bubble Scenario:

                An exception is thrown during the normal course of a method and should be handled by calling code.

            */

            throw new BusinessException("this exception is thrown in one method and expected to be handled in another.");
        }
    }

}
