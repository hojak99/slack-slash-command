package com.hojak99.slack.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SlashCommandController {

    @PostMapping("/messages")
    public void message() {
        log.info("Request 'POST /api/v1/messages'");
    }
}
