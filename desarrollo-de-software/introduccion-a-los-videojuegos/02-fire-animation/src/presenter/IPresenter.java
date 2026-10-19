package presenter;
import java.awt.image.BufferedImage;

import common.PaletteType;


public interface IPresenter {
    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------
    void start();
    // ------------------------------------------------------------------
    // Buttons behaviour
    // ------------------------------------------------------------------
    void play();
    void togglePause();
    void stop();
    // ------------------------------------------------------------------
    // Get render
    // ------------------------------------------------------------------
    BufferedImage getImage();
    // ------------------------------------------------------------------
    // Color settings
    // ------------------------------------------------------------------
    void setPaletteType(PaletteType type);   // paleta predefinida
    void setPaletteColor(int color);     // paleta generada desde un color
    // ------------------------------------------------------------------
    // Resolution settings
    // ------------------------------------------------------------------
    void setResolution(int w, int h);
    // ------------------------------------------------------------------
    // Fire settings
    // ------------------------------------------------------------------
    void setSparksPercentage(double p);
    void setCoolPointsPrecentage(double p);
    void setRandomization(int r);
    void setCoolingConstant(double c);
}