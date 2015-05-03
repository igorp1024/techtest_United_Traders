package com.theunitedtraders.javatest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.theunitedtraders.javatest.util.IdentificationUtils.threadId;

/**
 * Counting service. Counts requests amount.
 */
@Component
public class CountService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, Integer> countMap = new HashMap<>();

    /**
     * Registers request from user identified by <code>username</code>
     * @param username name to identify the user
     * @return count number for this request by this user.
     */
    public synchronized int count(String username) {

        int count = 1;
        if (countMap.containsKey(username)) {
            count = countMap.get(username) + 1;
        }
        countMap.put(username, count);

        logger.info("[{}] Request from {} #{}", threadId(), username, count);

        return count;
    }
}
