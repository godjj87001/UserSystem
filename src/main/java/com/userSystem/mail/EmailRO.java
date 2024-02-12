package com.userSystem.mail;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

@JsonPOJOBuilder
@Data
public class EmailRO {
    private String receiver;
    private String subject;
    private String content;
}
