package com.zhenhui.demo.artemis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandCoordinator {

    @NonNull
    private Command command;

    private volatile boolean signaled;

    public synchronized void waitComplete(long timeoutMillis) throws InterruptedException {
        this.wait(timeoutMillis);
    }

    public synchronized void signalComplete() {
        this.signaled = true;
        this.notifyAll();
    }

    public synchronized boolean signaled() {
        return this.signaled;
    }
}

