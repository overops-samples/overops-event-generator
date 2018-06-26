package veil.oo.test;

import com.takipi.sdk.v1.api.Takipi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import veil.oo.test.controller.Controller;
import veil.oo.test.domain.User;
import veil.oo.test.domain.UserRepository;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class OverOpsEventGeneratorApplication {

    private static final Logger log = LoggerFactory.getLogger(OverOpsEventGeneratorApplication.class);

    private static final int STARTUP_SLEEP = 10000;

    public static void main(String[] args) {
        SpringApplication.run(OverOpsEventGeneratorApplication.class, args);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ApplicationRunner createUsers(UserRepository repository) {
        return (args) -> {
            repository.save(new User("George", null, "Gordon", "lord.byron@gmail.com", LocalDate.of(1751, 12, 26), "London, England", "111-23-23123", "carelesschild", "George Gordon was born in London, England, third and youngest son of Cosmo George Gordon, 3rd Duke of Gordon, and the brother of Alexander Gordon, 4th Duke of Gordon"));
            repository.save(new User("Edgar", "Allan", "Poe", "edgar@gmail.com", LocalDate.of(1809, 1, 19), "Boston, Massachusetts", "222-23-4321", "theraven", "Poe was born in Boston, the second child of two actors. His father abandoned the family in 1810, and his mother died the following year."));
            repository.save(new User("Walt", null, "Whitman", "walt.whitman@gmail.com", LocalDate.of(1819, 5, 31), "Huntington, New York", "434-12-4216", "leavesofgrass", "Born in Huntington on Long Island, Whitman worked as a journalist, a teacher, a government clerk, and—in addition to publishing his poetry—was a volunteer nurse during the American Civil War."));

            log.info("Users found with findAll():");
            log.info("-------------------------------");
            for (User user : repository.findAll()) {
                log.info(user.toString());
            }
            log.info("");

            repository.findById(1L)
                    .ifPresent(user -> {
                        log.info("User found with findById(1L):");
                        log.info("--------------------------------");
                        log.info(user.toString());
                        log.info("");
                    });

            log.info("");
        };
    }

    @Bean
    Takipi buildTakipi() {
        return Takipi.create("OVEROPS_EVENT_GENERATOR");
    }

    @Bean
    @Profile("!test")
    public ApplicationRunner generateErrors(UserRepository repository, Controller controller) {
        return (args) -> {

            int userCount = (int) repository.count();

            log.info("sleeping for {} ms before starting", STARTUP_SLEEP);

            try {
                Thread.sleep(STARTUP_SLEEP);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            log.info("waking up and ready to generate errors");

            long events = -1;

            if (args.containsOption("oo.events")) {
                events = Long.parseLong(args.getOptionValues("oo.events").get(0));

                log.info("limiting number of events to {}", events);
            }


            AtomicLong invocationCounter = new AtomicLong(0);
            AtomicLong eventCounter = new AtomicLong(0);

            while (events == -1 || eventCounter.get() < events) {

                int randomUserId = ThreadLocalRandom.current().nextInt(1, userCount + 1);

                repository.findById((long) randomUserId).ifPresent(user -> {

                    boolean eventGenerated = false;

                    try {
                        eventGenerated = controller.route(invocationCounter.get(), user);
                    } catch (Exception e) {
                        log.error("THIS IS A BUG IN THE GENERATOR: " + e.getMessage(), e);
                    } finally {
                        if (eventGenerated) {
                            eventCounter.incrementAndGet();
                        }
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                });

                invocationCounter.incrementAndGet();

            }

            log.info("event generator finished!!!!  ran {} times and generated {} events.", invocationCounter.get(), eventCounter.get());
        };
    }
}
