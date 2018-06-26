package veil.oo.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import veil.oo.test.domain.User;


@Service
public class HttpService {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void throw500(User demoUser, boolean generateEvent) {

        log.trace("user details: {}", demoUser.toString());

        String url = "http://localhost:8080/throw500";

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("generateEvent", generateEvent);

        RestTemplate restTemplate = new RestTemplate();

        try {

            ResponseEntity<String> entity = restTemplate.getForEntity(builder.toUriString(), String.class);

            log.debug("throw500 get returned this value: {}", entity.getBody());

        } catch (RestClientException e) {

            // log as debug because i don't want another logged error or warn

            log.debug(e.getMessage(), e);
        }

    }

    public void throw404(User demoUser, boolean generateEvent) {

        log.trace("user details: {}", demoUser.toString());

        String url = "http://localhost:8080/throw404";

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("generateEvent", generateEvent);

        RestTemplate restTemplate = new RestTemplate();

        try {

            ResponseEntity<String> entity = restTemplate.getForEntity(builder.toUriString(), String.class);

            log.debug("throw404 get returned this value: {}", entity.getBody());

        } catch (RestClientException e) {

            // log as debug because i don't want another logged error or warn

            log.debug(e.getMessage(), e);
        }

    }
}
