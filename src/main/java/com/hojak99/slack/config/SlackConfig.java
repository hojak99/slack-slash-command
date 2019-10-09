package com.hojak99.slack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;

@Configuration
public class SlackConfig {
    @Bean
    public Mac createMacSha256() throws NoSuchAlgorithmException {
        return Mac.getInstance("HmacSHA256");
    }
}
