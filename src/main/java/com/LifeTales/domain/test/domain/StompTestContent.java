package com.LifeTales.domain.test.domain;

import lombok.Getter;
import lombok.Setter;

@Getter

public class StompTestContent {
    private String content;
    public StompTestContent(){
    }
    public StompTestContent(String content) {
        this.content = content;
    }
}
