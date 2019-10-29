package com.zhenhui.demo.artemis;

import org.apache.activemq.artemis.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.TimeoutException;

@Component
public class CommandExecutor implements MessageListener {

    public static final String DESTINATION = "DLQ";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private CoordinatorManager coordinatorManager;
    
    // 执行并等待
    public void execute(Command command, long timeout) throws InterruptedException, JmsException, TimeoutException {

        jmsTemplate.convertAndSend(DESTINATION, command);
        coordinatorManager.await(command, timeout);
    }

    // 监听执行结果，并结束等待
    @Override
    @JmsListener(destination = DESTINATION)
    public final void onMessage(Message message) {

        try {
            Thread.sleep(RandomUtil.randomInterval(2000, 6000));
            Command command = message.getBody(Command.class);
            coordinatorManager.signal(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



