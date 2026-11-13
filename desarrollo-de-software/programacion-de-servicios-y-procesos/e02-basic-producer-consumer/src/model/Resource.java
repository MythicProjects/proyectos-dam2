package model;

public class Resource {
    private static final int RETRY_MS = 10;   // sleep mode: time between checks

    private final int id;
    private final Config config;

    // volatile: the Presenter reads them from another thread
    private volatile int stock;
    private volatile long produced;
    private volatile long consumed;

    public Resource(int id, Config config) {
        this.id = id;
        this.config = config;
        this.stock = config.minStock();   // initial stock
    }

    // ------------------------------------------------------------------
    // Produce: chooses the version according to the configuration
    // ------------------------------------------------------------------
    public void produce(int amount) throws InterruptedException {
        if (!config.synchronize()) {
            produceUnsynchronized(amount);
        } else if (config.waitNotify()) {
            produceWaitNotify(amount);
        } else {
            produceSleep(amount);
        }
    }

    // wait/notify: the thread blocks until a consumer calls notifyAll()
    private synchronized void produceWaitNotify(int amount) throws InterruptedException {
        while (!canProduce(amount)) {
            wait();
        }
        add(amount);
        notifyAll();
    }

    // sleep: checks inside synchronized, sleeps outside (releasing the lock) and retries
    private void produceSleep(int amount) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (canProduce(amount)) {
                    add(amount);
                    return;
                }
            }
            Thread.sleep(RETRY_MS);
        }
    }

    // No synchronization: several threads can modify the stock at the same time
    private void produceUnsynchronized(int amount) throws InterruptedException {
        while (!canProduce(amount)) {
            Thread.sleep(RETRY_MS);
        }
        add(amount);
    }

    // ------------------------------------------------------------------
    // Consume: same three versions
    // ------------------------------------------------------------------
    public void consume(int amount) throws InterruptedException {
        if (!config.synchronize()) {
            consumeUnsynchronized(amount);
        } else if (config.waitNotify()) {
            consumeWaitNotify(amount);
        } else {
            consumeSleep(amount);
        }
    }

    private synchronized void consumeWaitNotify(int amount) throws InterruptedException {
        while (!canConsume(amount)) {
            wait();
        }
        remove(amount);
        notifyAll();
    }

    private void consumeSleep(int amount) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (canConsume(amount)) {
                    remove(amount);
                    return;
                }
            }
            Thread.sleep(RETRY_MS);
        }
    }

    private void consumeUnsynchronized(int amount) throws InterruptedException {
        while (!canConsume(amount)) {
            Thread.sleep(RETRY_MS);
        }
        remove(amount);
    }

    // ------------------------------------------------------------------
    // Stock control and the critical section
    // ------------------------------------------------------------------
    private boolean canProduce(int amount) {
        return !config.stockControl() || stock + amount <= config.maxStock();
    }

    private boolean canConsume(int amount) {
        return !config.stockControl() || stock - amount >= config.minStock();
    }

    // Read, pause, write: without synchronized, another thread can step in between
    private void add(int amount) {
        int current = stock;
        Thread.yield();
        stock = current + amount;
        produced += amount;
    }

    private void remove(int amount) {
        int current = stock;
        Thread.yield();
        stock = current - amount;
        consumed += amount;
    }

    // ------------------------------------------------------------------
    // Reads for the Presenter
    // ------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public long getProduced() {
        return produced;
    }

    public long getConsumed() {
        return consumed;
    }

    // What the stock should be if there were no race conditions
    public long getExpectedStock() {
        return config.minStock() + produced - consumed;
    }
}
