package model;

import java.awt.image.BufferedImage;

public class SpriteAnimation {
    private final BufferedImage[] frames;

    private volatile int frameIndex;
    private volatile int fps;

    private double pendingFrames;

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------

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

    // ------------------------------------------------------------------
    // Playback
    // ------------------------------------------------------------------

    // --- entity thread calls these
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

    // ------------------------------------------------------------------
    // Frame and speed
    // ------------------------------------------------------------------

    BufferedImage currentFrame(){
        return this.frames[this.frameIndex];
    }

    int fps(){
        return this.fps;
    }

    // --- Swing thread calls this (slider)
    void setFps(int fps){
        if (fps < 0){
            throw new IllegalArgumentException("fps cannot be negative: " + fps);
        }

        this.fps = fps;
    }
}
