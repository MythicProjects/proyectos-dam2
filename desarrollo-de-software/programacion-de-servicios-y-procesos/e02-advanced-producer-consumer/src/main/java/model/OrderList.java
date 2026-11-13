package model;

import java.util.ArrayDeque;
import java.util.Deque;

public class OrderList {
    private final Deque<Integer> orders = new ArrayDeque<>();

    // ------------------------------------------------------------------
    // Producer / consumer
    // ------------------------------------------------------------------
    // --- EDT (player) calls this function
    public synchronized void addOrder(int productId) {
        orders.addLast(productId);
        notifyAll();
    }

    // --- producer thread calls this function
    public synchronized int takeOrder() throws InterruptedException{
        while (orders.isEmpty()) {
            wait();
        }
        return orders.removeFirst();
    }

    // ------------------------------------------------------------------
    // State
    // ------------------------------------------------------------------
    public synchronized int size(){
        return orders.size();
    }

    public synchronized void clear(){
        orders.clear();
    }
}
