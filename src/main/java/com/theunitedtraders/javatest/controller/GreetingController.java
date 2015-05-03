package com.theunitedtraders.javatest.controller;

import com.theunitedtraders.javatest.domain.json.response.AuthorizedResponse;
import com.theunitedtraders.javatest.service.CountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.theunitedtraders.javatest.util.IdentificationUtils.threadId;

/**
 * Simple controller which greets the user.
 */
@RestController
@RequestMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class GreetingController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String KING_ROLE = "ROLE_ADMIN";
    private static final String USER_MESSAGE = "Welcome user!";
    private static final String ADMIN_MESSAGE = "Hail to the king!";

    @Autowired
    private CountService countService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AuthorizedResponse> view() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        logger.info("[{}] Got request from {}...", threadId(), username);

        String message = USER_MESSAGE;

        // Using Java 8 introduced streams API, lambdas, etc.
        // Doing this to conform the original task requirements: "Language level should be not lower than 1.6, 1.8 is better"
        Optional<? extends GrantedAuthority> result =
                auth.getAuthorities().stream().filter(authority -> KING_ROLE.equals(authority.getAuthority())).findAny();
        if (result.isPresent()) {
            message = ADMIN_MESSAGE;
        }

        AuthorizedResponse response = new AuthorizedResponse(message, countService.count(username));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
