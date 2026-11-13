package model;

import java.util.concurrent.ThreadLocalRandom;

public class Producer extends Thread {
    private final Resource resource;
    private final Config config;

    public Producer(int number, Resource resource, Config config) {
        super("Producer-" + number);
        this.resource = resource;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(randomTime(config.minProduceMs(), config.maxProduceMs()));   // producing
                resource.produce(config.produceAmount());
            }
        } catch (InterruptedException e) {
            // Stop was pressed: end the thread
        }
    }

    private static int randomTime(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
