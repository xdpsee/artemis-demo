package com.zhenhui.demo.artemis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
public class TestController {

    @Autowired
    private CommandExecutor executor;

    @Async
    @GetMapping("/test")
    public String test() {

        final Command command = new Command();
        command.setUuid(UUID.randomUUID().toString());
        command.setType("Q");

        try {
            executor.execute(command, 4000);
        } catch (Exception e) {
            if (e instanceof TimeoutException) {
                return "Timeout";
            }

            if (e instanceof JmsException) {
                return "JMS Exception";
            }

            if (e instanceof InterruptedException) {
                return "Interrupted Exception";
            }
        }

        return "SUCCESS";
    }

}
