package model;

import java.awt.image.BufferedImage;

public class Model implements IModel, Runnable{
    private enum State { STOPPED, RUNNING, PAUSED }
    private volatile State state = State.STOPPED;

    private volatile Thread thread;

    private static final int TARGET_FPS = 30;
    private static final long FRAME_MS = 1000 / TARGET_FPS;

    // Render (resolución lógica del modelo)
    private int width = 640;
    private int height = 360;
    private volatile BufferedImage image;

    public Model(){
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    // ------------------------------------------------------------------
    // MODEL THREAD
    // ------------------------------------------------------------------
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                switch (state) {
                    case STOPPED -> { thread = null; return; }
                    case PAUSED -> { }
                    case RUNNING -> updateModel();
                }
            }
            try{
                Thread.sleep(FRAME_MS);
            } catch(InterruptedException e){
                synchronized (this) {
                    state = State.STOPPED;
                }
            }
        }
    }
    // ------------------------------------------------------------------
    // UPDATE MODEL
    // ------------------------------------------------------------------
    private void updateModel(){

    }
    // ------------------------------------------------------------------
    // Buttons behaviour
    // ------------------------------------------------------------------
    // ---------------------- PLAY
    @Override 
    public synchronized void play(){
        state = State.RUNNING;

        if (thread == null) {
            thread = new Thread(this, "model");
            thread.setDaemon(true);
            thread.start();
        }
    }
    // ---------------------- STOP
    @Override 
    public synchronized void stop(){
        if (state != State.RUNNING && state != State.PAUSED) return; // ya parado o apagándose

        state = State.STOPPED;
    }
    // ---------------------- PAUSE
    @Override 
    public synchronized void togglePause(){
        if (state == State.RUNNING) {
            state = State.PAUSED;
        } else if (state == State.PAUSED) {
            state = State.RUNNING;
        }
    }
    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    @Override public BufferedImage getImage() { return image; }
    @Override public synchronized int getWidth() { return width; }
    @Override public synchronized int getHeight() { return height; }
    // ------------------------------------------------------------------
    // Resolution settings
    // ------------------------------------------------------------------
    @Override
    public synchronized void setResolution(int width, int height){
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
}
