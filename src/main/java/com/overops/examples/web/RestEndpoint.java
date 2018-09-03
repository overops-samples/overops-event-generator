package com.overops.examples.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.overops.examples.error.UncaughtException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RestEndpoint {

    public static final String GENERATE_EVENT = "generateEvent";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(path = "/throw500", method = RequestMethod.GET)
    public String throw500(HttpServletRequest request, HttpServletResponse response) {

        String generateEvent = request.getParameter(GENERATE_EVENT);

        if (generateEvent != null && generateEvent.equalsIgnoreCase(Boolean.TRUE.toString())) {

            try {

                log.debug("going to call response.sendError() with {}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "an error 500 occurred in the webservice");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }

        return "no 500 thrown this time";
    }

    @RequestMapping(path = "/throw404", method = RequestMethod.GET)
    public String throw404(HttpServletRequest request, HttpServletResponse response) {

        String generateEvent = request.getParameter(GENERATE_EVENT);

        if (generateEvent != null && generateEvent.equalsIgnoreCase(Boolean.TRUE.toString())) {
            try {

                log.debug("going to call response.sendError() with {}", HttpServletResponse.SC_NOT_FOUND);

                response.sendError(HttpServletResponse.SC_NOT_FOUND, "a 404 occurred");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }

        return "no 404 thrown this time";
    }
    
    @RequestMapping(path = "/uncaught", method = RequestMethod.GET)
    public String uncaught(HttpServletRequest request, HttpServletResponse response) {

        String generateEvent = request.getParameter(GENERATE_EVENT);

        if (generateEvent != null && generateEvent.equalsIgnoreCase(Boolean.TRUE.toString())) {
            log.debug("going to throw uncaught");
            
            throw new RuntimeException("Uncaught");
        }

        return "no 404 thrown this time";
    }
}
