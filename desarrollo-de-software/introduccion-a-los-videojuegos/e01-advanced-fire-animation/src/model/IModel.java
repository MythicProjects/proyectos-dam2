package model;

import java.awt.image.BufferedImage;

import common.PaletteType;

public interface IModel {
    // ------------------------------------------------------------------
    // Buttons behaviour
    // ------------------------------------------------------------------
    void play();
    void stop();
    void togglePause();
    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    BufferedImage getImage();
    int getWidth();
    int getHeight();
    double getSparksPercentage();
    double getCoolPointsPercentage();
    int getRandomization();
    // ------------------------------------------------------------------
    // Color settings
    // ------------------------------------------------------------------
    void setPaletteType(PaletteType type);
    void setPaletteColor(int color);
    // ------------------------------------------------------------------
    // Resolution settings
    // ------------------------------------------------------------------
    void setResolution(int w, int h);
    // ------------------------------------------------------------------
    // Fire settings
    // ------------------------------------------------------------------
    void setSparksPercentage(double p);
    void setCoolPointsPercentage(double p);
    void setRandomization(int r);
    void setCoolingConstant(double c);
}
