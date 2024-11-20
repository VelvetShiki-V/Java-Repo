package com.example.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class a {

    private static final Logger logger = LoggerFactory.getLogger(a.class);


    @RequestMapping("/")
    public void index() {
        logger.info("123");
    }
}
