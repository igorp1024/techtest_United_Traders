package com.theunitedtraders.javatest.controller.handler;

import com.theunitedtraders.javatest.domain.json.response.base.SimpleMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception handler implemented as controller. We could have implement this exceptions handlers as custom
 * spring security entry point or filter, but this way is cleaner a bit from code perspective.
 */
@RestController
@RequestMapping(value = "/error", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class WebApplicationExceptionHandlerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/resourceNotFound")
    @ResponseBody
    public ResponseEntity<SimpleMessageResponse> resourceNotFound(HttpServletRequest request) {
        logger.info("Requested resource is not found");
        SimpleMessageResponse response = new SimpleMessageResponse("Resource not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/unauthorized")
    @ResponseBody
    public ResponseEntity<SimpleMessageResponse> unauthorized(HttpServletRequest request) {
        logger.info("Request is not authorized");
        SimpleMessageResponse response = new SimpleMessageResponse("Access denied");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/forbidden")
    @ResponseBody
    public ResponseEntity<SimpleMessageResponse> forbidden(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        logger.info("Request is forbidden for {}", username);
        SimpleMessageResponse response =
                new SimpleMessageResponse(
                        String.format("User %s does not have access", username)
                );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
