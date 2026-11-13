package model;

import java.util.concurrent.ThreadLocalRandom;

public class Consumer extends Thread {
    private final Resource resource;
    private final Config config;

    public Consumer(int number, Resource resource, Config config) {
        super("Consumer-" + number);
        this.resource = resource;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(randomTime(config.minConsumeMs(), config.maxConsumeMs()));   // consuming
                resource.consume(config.consumeAmount());
            }
        } catch (InterruptedException e) {
            // Stop was pressed: end the thread
        }
    }

    private static int randomTime(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
