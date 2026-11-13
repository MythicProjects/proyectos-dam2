package model;

import java.util.List;

public class Consumer extends Thread {
    private static final long TICK_MS = 50;

    private final int seat;
    private final double eatSeconds;
    private final Resource resource;
    private final List<Product> products;
    private final Wallet wallet;
    private final Clock clock;

    private volatile int productId = -1;
    private volatile float progress;

    public Consumer(int seat, double eatSeconds, Resource resource, List<Product> products,
                    Wallet wallet, Clock clock) {
        super("Consumer-" + seat);
        setDaemon(true);
        this.seat = seat;
        this.eatSeconds = eatSeconds;
        this.resource = resource;
        this.products = products;
        this.wallet = wallet;
        this.clock = clock;
    }

    // ------------------------------------------------------------------
    // Thread
    // ------------------------------------------------------------------
    @Override
    public void run(){
        try {
            clock.awaitRunning();
            productId = resource.take();
            eat();
            wallet.earn(products.get(productId).getSalePrice());
        } catch (InterruptedException e){
        }
    }

    private void eat() throws InterruptedException {
        double elapsed = 0;
        progress = 0;
        while (elapsed < eatSeconds) {
            clock.awaitRunning();
            Thread.sleep(TICK_MS);
            elapsed += TICK_MS / 1000.0 * clock.getSpeed();
            progress = (float) Math.min(1.0, elapsed / eatSeconds);
        }
    }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    // --- EDT (presenter) calls these functions
    public int getSeat() { return seat; }
    public boolean isBusy() { return productId >= 0; }
    public int getProductId() { return productId; }
    public float getProgress() { return progress; }
}
