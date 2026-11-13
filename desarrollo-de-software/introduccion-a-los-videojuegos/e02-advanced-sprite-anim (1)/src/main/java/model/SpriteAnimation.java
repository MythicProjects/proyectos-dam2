package model;

import java.awt.image.BufferedImage;

public class SpriteAnimation {
    private final BufferedImage[] frames;

    private volatile int frameIndex;
    private volatile int fps;

    private double pendingFrames;

    SpriteAnimation(BufferedImage[] frames, int fps){
        if (frames == null || frames.length == 0){
            throw new IllegalArgumentException("An animation needs at least one frame");
        }

        if (fps < 0) {
            throw new IllegalArgumentException("fps cannot be negative: " + fps);
        }
        this.frames = frames;
        this.fps = fps;
    }

    void advance(double elapsedSeconds){
        if (this.frames.length == 1){
            return;
        }

        this.pendingFrames += elapsedSeconds * this.fps;
        int steps = (int) this.pendingFrames;
        if (steps > 0){
            this.pendingFrames -= steps;
            this.frameIndex = (this.frameIndex + steps) % this.frames.length;
        }
    }

    void reset(){
        this.frameIndex = 0;
        this.pendingFrames = 0;
    }

    BufferedImage currentFrame(){
        return this.frames[this.frameIndex];
    }

    int fps(){
        return this.fps;
    }

    void setFps(int fps){
        if (fps < 0){
            throw new IllegalArgumentException("fps cannot be negative: " + fps);
        }

        this.fps = fps;
    }
}
