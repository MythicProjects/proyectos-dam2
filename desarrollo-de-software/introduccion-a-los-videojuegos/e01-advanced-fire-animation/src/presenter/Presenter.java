package presenter;
import model.IModel;
import view.IView;
import java.awt.image.BufferedImage;

import common.PaletteType;

public class Presenter implements IPresenter {
    // Model
    private final IModel model;

    // View
    private final IView view;
    // --- view settings
    private final int MIN_WIDTH = 16;
    private final int MIN_HEIGHT = 16;

    public Presenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------
    @Override
    public void start() {
        view.setPresenter(this);
        view.showValues(model.getWidth(), model.getHeight(), 
            (int) model.getSparksPercentage(), 
            (int) model.getCoolPointsPercentage(), 
            model.getRandomization());
        view.startView(); // prepare window
        model.play(); // start thread
    }
    // ------------------------------------------------------------------
    // Buttons behaviour
    // ------------------------------------------------------------------
    @Override public void play() { model.play(); }
    @Override public void togglePause() { model.togglePause(); }
    @Override public void stop() { model.stop(); }
    // ------------------------------------------------------------------
    // Get render
    // ------------------------------------------------------------------
    @Override public BufferedImage getImage() { return model.getImage(); }
    // ------------------------------------------------------------------
    // Color settings
    // ------------------------------------------------------------------
    @Override public void setPaletteType(PaletteType t) { model.setPaletteType(t); }
    @Override public void setPaletteColor(int c) { model.setPaletteColor(c); }
    // ------------------------------------------------------------------
    // Resolution settings
    // ------------------------------------------------------------------
    @Override public void setResolution(int w, int h) {
        if (w < MIN_WIDTH || h < MIN_HEIGHT){
            view.showMessage("Resolución mínima: " + MIN_WIDTH + "×" + MIN_HEIGHT);
            return;  
        } 
        model.setResolution(w, h);
    }
    // ------------------------------------------------------------------
    // Fire settings
    // ------------------------------------------------------------------
    @Override public void setSparksPercentage(double p) { model.setSparksPercentage(clamp(p, 0, 100)); }
    @Override public void setCoolPointsPrecentage(double p) { model.setCoolPointsPercentage(clamp(p, 0, 100)); }
    @Override public void setRandomization(int r) {model.setRandomization(r);}
    @Override public void setCoolingConstant(double c) {model.setCoolingConstant(c);}
    // ------------------------------------------------------------------
    // Settings
    // ------------------------------------------------------------------
    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}