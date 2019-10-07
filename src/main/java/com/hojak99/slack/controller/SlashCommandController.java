package com.hojak99.slack.controller;

import com.hojak99.slack.dto.SlashCommandRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SlashCommandController {

    @PostMapping(value = "/messages", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void message(SlashCommandRequest dataPayload) {
        log.info("Request 'POST /api/v1/messages' request: {}", dataPayload);
    }
}
