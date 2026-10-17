package model;

import java.awt.image.BufferedImage;

import common.PaletteType;

public class Model implements IModel, Runnable{
    private static final int DEFAULT_WIDTH = 720;
    private static final int DEFAULT_HEIGHT = 180;
    private final FireGenerator fire;
    private static final int TARGET_FPS = 30;
    private static final long FRAME_MS = 1000 / TARGET_FPS;
    private static final int EXTINGUISH_FRAMES = 2 * TARGET_FPS;
    
    private double extinguishingDecrement;
    private volatile double sparksPercentage = 100; // el valor que eligió el usuario
    private double currentSparks;          // el que se va reduciendo al apagar
    
    private final Render render;
    
    private volatile Thread thread;
    
    private enum State { STOPPED, RUNNING, PAUSED, EXTINGUISHING }

    private volatile State state = State.STOPPED;

    public Model(){
        fire = new FireGenerator(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        fire.setSparksPercentage(100);
        fire.setCoolPointsPercentage(70);
        fire.setRandomization(50);

        render = new Render(fire, Palette.createWithType(PaletteType.FIRE));
        render.renderFrame();
    }
    

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                switch (state) {
                    case STOPPED -> { thread = null; return; }  // termina el hilo
                    case PAUSED -> { } // no actualiza: la imagen se queda congelada
                    case RUNNING -> updateModel();
                    case EXTINGUISHING -> {
                        extinguishingStep();
                        updateModel();
                    }
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

    private void updateModel(){
        fire.setSparkPoints();
        fire.setCoolPoints();
        fire.updateTemperatures();

        render.renderFrame();
    }

    private void extinguishingStep(){
        currentSparks = Math.max(0, currentSparks - extinguishingDecrement);
        fire.setSparksPercentage(currentSparks);

        if(currentSparks == 0){
            fire.clearBaseRow();
            if(fire.isCold()) state = State.STOPPED;
        }
    }

    @Override 
    public synchronized void play(){
        state = State.RUNNING;
        fire.setSparksPercentage(sparksPercentage);

        if (thread == null) {
            thread = new Thread(this, "fire-model");
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override 
    public synchronized void stop(){
        if (state != State.RUNNING && state != State.PAUSED) return; // ya parado o apagándose
        currentSparks = sparksPercentage;
        extinguishingDecrement = Math.max(sparksPercentage, 1.0) / EXTINGUISH_FRAMES;
        state = State.EXTINGUISHING;
    }

    @Override 
    public synchronized void togglePause(){
        if (state == State.RUNNING) {
            state = State.PAUSED;
        } else if (state == State.PAUSED) {
            state = State.RUNNING;
        }
    }

    @Override 
    public BufferedImage getImage() { return render.getFrontImage(); }

    @Override public int getWidth() { return fire.getWidth(); }
    @Override public int getHeight() { return fire.getHeight(); }
    @Override public double getSparksPercentage() { return sparksPercentage; }
    @Override public double getCoolPointsPercentage() { return fire.getCoolPointsPercentage(); }
    @Override public int getRandomization() { return fire.getRandomization(); }

// Model
@Override
    public synchronized void setPaletteType(PaletteType type) {
        render.setPalette(Palette.createWithType(type));
        render.renderFrame();
    }
    @Override
    public synchronized void setPaletteColor(int color) {
        render.setPalette(Palette.createWithColor(color));
        render.renderFrame(); // para que se vea al momento, aunque esté en pausa
    }
    @Override 
    public synchronized void setResolution(int w, int h){
        fire.setResolution(w, h);
    }
    @Override
    public synchronized void setSparksPercentage(double p) {
        sparksPercentage = p;
        if (state != State.EXTINGUISHING) fire.setSparksPercentage(p);
    }
    @Override 
    public synchronized void setCoolPointsPercentage(double p){
        fire.setCoolPointsPercentage(p);
    }
    @Override 
    public synchronized void setRandomization(int r){
        fire.setRandomization(r);
    }
    public synchronized void setCoolingConstant(double c){
        fire.setCooling(c);
    }
}
