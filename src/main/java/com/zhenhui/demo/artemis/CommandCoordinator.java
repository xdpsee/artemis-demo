package com.zhenhui.demo.artemis;

public class CommandCoordinator {

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

