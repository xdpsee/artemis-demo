package com.zhenhui.demo.artemis;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class CoordinatorManager {

    private static final Logger logger = LoggerFactory.getLogger(CoordinatorManager.class);

    private Cache<Command, CommandCoordinator> coordinators = CacheBuilder.newBuilder()
            .concurrencyLevel(16)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .initialCapacity(1024)
            .build();

    public void await(Command command, long timeout) throws InterruptedException, TimeoutException {

        CommandCoordinator coordinator = new CommandCoordinator();
        coordinators.put(command, coordinator);

        logger.info("execute command, waiting");
        coordinator.waitComplete(timeout);
        coordinators.invalidate(command);
        
        if (coordinator.signaled()) {
            logger.info("execute command, complete");
        } else {
            logger.info("execute command, timeout");
            throw new TimeoutException("execute command, timeout");
        }
    }

    public void signal(Command command) {
        CommandCoordinator coordinator = coordinators.getIfPresent(command);
        if (coordinator != null) {
            logger.info("execute command, signal");
            coordinator.signalComplete();
        } else {
            logger.info("execute command, signal, but no coordinator found");
        }
    }
}

