package model;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements IModel {
    private final List<Resource> resources = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();

    @Override
    public void start(Config config) {
        stop();
        resources.clear();

        for (int i = 0; i < config.resourceTypes(); i++) {
            resources.add(new Resource(i + 1, config));
        }

        // Each thread works on one resource type, assigned in turns
        for (int i = 0; i < config.producers(); i++) {
            threads.add(new Producer(i + 1, resources.get(i % resources.size()), config));
        }
        for (int i = 0; i < config.consumers(); i++) {
            threads.add(new Consumer(i + 1, resources.get(i % resources.size()), config));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public void stop() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
    }

    @Override
    public boolean isRunning() {
        return !threads.isEmpty();
    }

    @Override
    public List<Resource> getResources() {
        return List.copyOf(resources);
    }
}
