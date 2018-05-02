package veil.oo.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import veil.oo.test.domain.User;


@Service
public class HttpService {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void throw500(User demoUser, boolean generateEvent) {

        log.trace("user details: {}", demoUser.toString());

        if (generateEvent) {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/throw500", String.class);

            log.debug("HTTP get returned this value {}", entity.getBody());
        }
    }

    public void throw404(User demoUser, boolean generateEvent) {

        log.trace("user details: {}", demoUser.toString());

        if (generateEvent) {
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getForEntity("http://localhost:8080/throw404", String.class);

        }
    }
}
