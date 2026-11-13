package model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Resource {
    private final int capacity;
    private final Deque<Integer> units = new ArrayDeque<>();
    private final int[] countByProduct;

    private long totalProduced;
    private long totalConsumed;

    public Resource(int capacity, int producCount){
        this.capacity = capacity;
        this.countByProduct = new int[producCount];
    }

    // ------------------------------------------------------------------
    // Producer / consumer
    // ------------------------------------------------------------------
    // --- producer thread calls this function
    public synchronized void put(int productId) throws InterruptedException {
        while (units.size() >= capacity) {
            wait();
        }
        units.addLast(productId);
        countByProduct[productId]++;
        totalProduced++;
        notifyAll();
    }

    // --- consumer thread calls this function
    public synchronized int take() throws InterruptedException {
        while (units.isEmpty()) {
            wait();
        }
        int productId = units.removeFirst();
        countByProduct[productId]--;
        totalConsumed++;
        notifyAll();
        return productId;
    }

    public synchronized void clear(){
        units.clear();
        java.util.Arrays.fill(countByProduct, 0);
        totalProduced = 0;
        totalConsumed = 0;
        notifyAll();
    }

    // ------------------------------------------------------------------
    // Snapshot reads
    // ------------------------------------------------------------------
    // --- EDT (presenter) calls these functions
    public synchronized int getStock() {
        return units.size();
    }

    public synchronized int[] getUnits() {
        return units.stream().mapToInt(Integer::intValue).toArray();
    }

    public synchronized int[] getCountByProduct() {
        return countByProduct.clone();
    }

    public int getCapacity() {
        return capacity;
    }

    // ------------------------------------------------------------------
    // Statistics
    // ------------------------------------------------------------------
    // --- world thread calls these functions
    public synchronized long getTotalProduced() {
        return totalProduced;
    }

    public synchronized long getTotalConsumed() {
        return totalConsumed;
    }
}
