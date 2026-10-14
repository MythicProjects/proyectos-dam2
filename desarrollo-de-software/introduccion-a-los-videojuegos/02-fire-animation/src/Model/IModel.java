package Model;

import java.awt.image.BufferedImage;

public interface IModel {
    void update();
    BufferedImage getImage();
    void setPalette(Palette palette);
    void setResolution(int width, int height);
    void setSparksPercentage(double percentage);
    void setCoolPointsPercentage(double percentage);
    void setRandomization(int randomization);
}
