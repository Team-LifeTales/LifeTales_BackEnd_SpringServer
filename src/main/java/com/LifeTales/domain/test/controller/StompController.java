package com.LifeTales.domain.test.controller;

import com.LifeTales.domain.test.domain.StompTest;
import com.LifeTales.domain.test.domain.StompTestContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@Slf4j
public class StompController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public StompTestContent stomp(StompTest message) throws Exception {
        log.info("name : {}" , message.getName());
        Thread.sleep(1000); // simulated delay
        return new StompTestContent("hello" +message.getName() +"!");
    }
}
