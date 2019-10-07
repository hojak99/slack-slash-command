package com.hojak99.slack.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SlashCommandRequest {
    private String token;
    private String team_id;
    private String team_domain;
    private String enterprise_id;
    private String enterprise_name;
    private String channel_id;
    private String channel_name;
    private String user_id;
    private String user_name;
    private String command;
    private String text;
    private String response_url;
    private String trigger_Id;
}