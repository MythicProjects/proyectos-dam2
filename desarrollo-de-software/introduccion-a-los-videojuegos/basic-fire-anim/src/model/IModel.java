package src.model;

import java.awt.image.BufferedImage;

public interface IModel {
    void start();
    void update();
    BufferedImage getImage();
}