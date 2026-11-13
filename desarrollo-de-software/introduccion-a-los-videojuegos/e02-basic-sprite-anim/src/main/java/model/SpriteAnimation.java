package model;

import java.awt.image.BufferedImage;

/** One animation on screen: shared frames, its own position, size, speed and thread. */
final class SpriteAnimation implements Runnable {

    private final String name;
    private final BufferedImage[] frames;   // shared through the pool, never modified
    private final int x;
    private final int y;

    // Written by one thread, read by another
    private volatile int frameIndex;
    private volatile int fps;
    private volatile int scale;

    SpriteAnimation(String name, BufferedImage[] frames, int x, int y, int fps, int scale) {
        this.name = name;
        this.frames = frames;
        this.x = x;
        this.y = y;
        this.fps = fps;
        this.scale = scale;
    }

    void start() {
        Thread thread = new Thread(this, "Animation " + this.name);
        thread.setDaemon(true);             // closes with the window
        thread.start();
    }

    /** Wait one frame, show the next one, repeat. */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 / this.fps);
            } catch (InterruptedException e) {
                return;
            }
            this.frameIndex = (this.frameIndex + 1) % this.frames.length;   // loops back to 0
        }
    }

    String name() { return this.name; }
    BufferedImage frame() { return this.frames[this.frameIndex]; }
    int x() { return this.x; }
    int y() { return this.y; }
    int fps() { return this.fps; }
    int scale() { return this.scale; }

    void setFps(int fps) { this.fps = Math.max(1, fps); }   // 0 would divide by zero
    void setScale(int scale) { this.scale = Math.max(1, scale); }
}
