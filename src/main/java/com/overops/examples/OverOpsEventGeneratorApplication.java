package com.overops.examples;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.overops.examples.controller.Controller;
import com.overops.examples.domain.User;
import com.overops.examples.domain.UserRepository;
import com.takipi.sdk.v1.api.Takipi;

import io.sentry.overops.examples.utils.SentryUtil;

@SpringBootApplication
@EnableAsync
public class OverOpsEventGeneratorApplication {

    private static final Logger log = LoggerFactory.getLogger(OverOpsEventGeneratorApplication.class);

    private static final int STARTUP_SLEEP = 10000;

    public static void main(String[] args) {
        SpringApplication.run(OverOpsEventGeneratorApplication.class, args);
    }
    
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
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

    @Async("taskExecutor")
    public void createUserWithConcurrentExecutor(){
    	System.out.println("Currently Executing thread name - " + Thread.currentThread().getName());
    	System.out.println("User created with concurrent task executor");
    }
    
    @Bean
    @Profile("!test")
    public ApplicationRunner generateErrors(UserRepository repository, Controller controller) {
        return (args) -> {
            if (args.containsOption("oo.sentry")) {
                boolean sentry = Boolean.parseBoolean(args.getOptionValues("oo.sentry").get(0));
                
                if (sentry) {
                	SentryUtil.enable();
                	log.info("Sentry enabled");
                }
            }
            
            log.info("sleeping for {} ms before starting", STARTUP_SLEEP);
            
            try {
                Thread.sleep(STARTUP_SLEEP);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            log.info("waking up and ready to generate errors");

            long events = -1;
            long invocations = -1;

            if (args.containsOption("oo.events")) {
                events = Long.parseLong(args.getOptionValues("oo.events").get(0));

                log.info("limiting number of events to {}", events);
            }
            
            if (args.containsOption("oo.invocations")) {
                invocations = Long.parseLong(args.getOptionValues("oo.invocations").get(0));

                log.info("limiting number of invocations to {}", invocations);
            }

            AtomicLong invocationCounter = new AtomicLong(0);
            AtomicLong eventCounter = new AtomicLong(0);
            
            int userCount = (int) repository.count();
            
            long start = System.currentTimeMillis();
            
            while ((events == -1 || eventCounter.get() < events) &&
            	  ((invocations == -1 || invocationCounter.get() < invocations))) {
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
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                });

                invocationCounter.incrementAndGet();
            }

            long end = System.currentTimeMillis();

            log.info("EVENTS FINISHED!!!! ran for {} ms, {} times and generated {} events.",
              (end-start), invocationCounter.get(), eventCounter.get());
        };
    }
}
