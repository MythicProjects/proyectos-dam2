package model;

import java.awt.image.BufferedImage;

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
    // ------------------------------------------------------------------
    // Resolution settings
    // ------------------------------------------------------------------
    void setResolution(int width, int height);
}
