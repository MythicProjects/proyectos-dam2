package contract;

import java.awt.image.BufferedImage;

public interface IModel {

    // ------------------------------------------------------------------
    // Lifecycle
    // ------------------------------------------------------------------

    void setListener(IModelListener listener);
    void start();

    // ------------------------------------------------------------------
    // Scene
    // ------------------------------------------------------------------

    BufferedImage background();

    // ------------------------------------------------------------------
    // Entities (read-only, by index)
    // ------------------------------------------------------------------

    int entityCount();
    BufferedImage frame(int index);
    int x(int index);
    int y(int index);
    String name(int index);
    int fps(int index);

    // ------------------------------------------------------------------
    // Player
    // ------------------------------------------------------------------

    int playerFps();
    void setPlayerFps(int fps);
    int playerMoveSpeed();
    void setPlayerMoveSpeed(int pixelsPerSecond);
    void movePlayer(Direction direction);
    void stopPlayer();
}
