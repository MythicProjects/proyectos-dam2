package contract;

import java.awt.image.BufferedImage;
import java.util.List;

public interface IPresenter {

    void start();

    // Drawing
    BufferedImage background();
    List<AnimationView> animationViews();
    int entityCount();

    // Speed limits for the sliders
    int minFps();
    int maxFps();
    int minMoveSpeed();
    int maxMoveSpeed();

    // Speeds
    void setRenderSpeed(int fps);
    int playerSpeed();
    void setPlayerSpeed(int fps);
    int playerMoveSpeed();
    void setPlayerMoveSpeed(int pixelsPerSecond);

    // Player input
    void movePlayer(Direction direction);
    void stopPlayer();
}