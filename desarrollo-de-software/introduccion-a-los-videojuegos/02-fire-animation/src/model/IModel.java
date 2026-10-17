package model;

import java.awt.image.BufferedImage;

import common.PaletteType;

public interface IModel {

    
    void play();
    void stop();
    void togglePause();
    
    BufferedImage getImage();
    int getWidth();
    int getHeight();
    double getSparksPercentage();
    double getCoolPointsPercentage();
    int getRandomization();
    
    void setPaletteType(PaletteType type);   // paleta predefinida
    void setPaletteColor(int color);     // paleta generada desde un color
    void setResolution(int w, int h);
    void setSparksPercentage(double p);
    void setCoolPointsPercentage(double p);
    void setRandomization(int r);
    void setCoolingConstant(double c);
}
