package com.userSystem.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

@Data
public class EmailRO {
    private String receiver;
    private String subject;
    private String content;
}
