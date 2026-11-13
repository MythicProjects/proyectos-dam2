package model;

public class Producer extends Thread{
    private static final long TICK_MS = 50;
    private static final double UPGRADE_FACTOR = 0.8;

    private final int slot;
    private final double baseCycleSeconds;
    private final OrderList orders;
    private Resource resource;
    private final Clock clock;

    private volatile int level = 1;
    private volatile int productId = -1;
    private volatile float progress;

    public Producer(int slot, double baseCycleSeconds, OrderList orders, Resource resource, Clock clock) {
        super("Producer-" + slot);
        setDaemon(true);
        this.slot = slot;
        this.baseCycleSeconds = baseCycleSeconds;
        this.orders = orders;
        this.resource = resource;
        this.clock = clock;
    }

    // ------------------------------------------------------------------
    // Thread
    // ------------------------------------------------------------------
    @Override
    public void run(){
        try {
            while (!isInterrupted()) {
                productId = orders.takeOrder();
                produce();
                resource.put(productId);
                productId = -1;
                progress = 0;
            }
        } catch (InterruptedException e) {
        }
    }

    private void produce() throws InterruptedException {
        double elapsed = 0;
        progress = 0;
        while (elapsed < getCycleSeconds()) {
            clock.awaitRunning();
            Thread.sleep(TICK_MS);
            elapsed += TICK_MS / 1000.0 * clock.getSpeed();
            progress = (float) Math.min(1.0, elapsed / getCycleSeconds());
        }
    }

    // ------------------------------------------------------------------
    // Upgrades
    // ------------------------------------------------------------------
    // --- EDT (player) calls this function
    public void upgrade() {
        level++;
    }

    public double getCycleSeconds(){
        return baseCycleSeconds * Math.pow(UPGRADE_FACTOR, level -1);
    }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    // --- EDT (presenter) calls these functions
    public int getSlot() {
        return slot;
    }

    public int getLevel() {
        return level;
    }

    public int getProductId() {
        return productId;
    }

    public float getProgress() {
        return progress;
    }
}
