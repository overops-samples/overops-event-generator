package veil.oo.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import veil.oo.test.domain.User;
import veil.oo.test.service.*;

import java.util.Random;

@Component
public class Controller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Random rand = new Random();

    private final CatchAndProcessService catchAndProcessService;

    private final CatchAndIgnoreService catchAndIgnoreService;

    private final LoggedErrorService loggedErrorService;

    private final CustomEventService customEventService;

    private final SlowService slowService;

    private final LoggedWarnService loggedWarnService;

    private final VeryBrokenService veryBrokenService;

    private final UncaughtExceptionService uncaughtExceptionService;

    private final HttpService httpService;


    @Autowired
    public Controller(CatchAndProcessService catchAndProcessService, CatchAndIgnoreService catchAndIgnoreService, LoggedErrorService loggedErrorService, CustomEventService customEventService, SlowService slowService, LoggedWarnService loggedWarnService, VeryBrokenService veryBrokenService, UncaughtExceptionService uncaughtExceptionService, HttpService httpService) {
        this.catchAndProcessService = catchAndProcessService;
        this.catchAndIgnoreService = catchAndIgnoreService;
        this.loggedErrorService = loggedErrorService;
        this.customEventService = customEventService;
        this.slowService = slowService;
        this.loggedWarnService = loggedWarnService;
        this.veryBrokenService = veryBrokenService;
        this.uncaughtExceptionService = uncaughtExceptionService;
        this.httpService = httpService;
    }

    public boolean route(long counter, User user) {

        log.trace("counter is {}", counter);

        log.info("user is {}", user.toString());

        boolean generateEvent = false;

        if (counter != 0 && counter % 5 == 0) {
            generateEvent = true;
        }

        log.info("generate event? {}", generateEvent);

        int scenario = rand.nextInt(9) + 1;

        log.debug("event scenario is {}", scenario);

        if (scenario == 1) {

            catchAndProcessService.handleException(user, generateEvent);

        } else if (scenario == 2) {

            catchAndIgnoreService.catchAndIgnore(user, generateEvent);

        } else if (scenario == 3) {

            loggedWarnService.warningsAbound(user, generateEvent);

        } else if (scenario == 4) {

            slowService.longRunningMethod(user, generateEvent);

        } else if (scenario == 5) {

            customEventService.fireCustomEvent(user, generateEvent);

        } else if (scenario == 6) {

            loggedErrorService.errorsAbound(user, generateEvent);

        } else if (scenario == 7) {

            uncaughtExceptionService.cantCatchMe(user, generateEvent);

        } else if (scenario == 8) {

            httpService.throw500(user, generateEvent);

        } else if (scenario == 9) {

            httpService.throw404(user, generateEvent);

        } else {

            log.warn("{} is an invalid scenario", scenario);

        }

        return generateEvent;


    }

}
