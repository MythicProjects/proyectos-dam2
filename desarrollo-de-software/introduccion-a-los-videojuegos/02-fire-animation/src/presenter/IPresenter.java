package presenter;
import java.awt.image.BufferedImage;

import common.PaletteType;


public interface IPresenter {
    void start();
    void play();
    void togglePause();
    void stop();
    BufferedImage getImage();
    void setPaletteType(PaletteType type);   // paleta predefinida
    void setPaletteColor(int color);     // paleta generada desde un color
    void setResolution(int w, int h);
    void setSparksPercentage(double p);
    void setCoolPointsPrecentage(double p);
    void setRandomization(int r);
    void setCoolingConstant(double c);
}