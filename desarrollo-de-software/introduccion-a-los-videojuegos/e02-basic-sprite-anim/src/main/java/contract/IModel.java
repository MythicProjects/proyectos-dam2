package contract;

import java.awt.image.BufferedImage;

/** What the presenter can ask the model. */
public interface IModel {
    void start();                       // starts one thread per animation

    int count();
    String name(int index);
    BufferedImage frame(int index);     // current frame of that animation
    int x(int index);
    int y(int index);
    int scale(int index);
    int fps(int index);

    void setScale(int index, int scale);
    void setFps(int index, int fps);
}
