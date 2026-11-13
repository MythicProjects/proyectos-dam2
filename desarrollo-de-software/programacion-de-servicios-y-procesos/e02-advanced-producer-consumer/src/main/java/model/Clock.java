package model;

public class Clock {
    private boolean paused = false;
    private int speed = 1;

    // ------------------------------------------------------------------
    // Controls
    // ------------------------------------------------------------------
    // --- EDT calls this function
    public synchronized void play(){
        paused = false;
        speed = 1;
        notifyAll();
    }

    // --- EDT calls this function
    public synchronized void pause(){ paused = true; }

    // --- EDT calls this function
    public synchronized void fastForward () {
        paused = false;
        speed = 2;
        notifyAll();
    }

    // ------------------------------------------------------------------
    // Synchronization
    // ------------------------------------------------------------------
    // --- producer, consumer and world threads call this function
    public synchronized void awaitRunning() throws InterruptedException {
        while (paused) {
            wait();
        }
    }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    public synchronized int getSpeed(){
        return speed;
    }

    public synchronized boolean isPaused(){
        return paused;
    }
}
