package contract;

import java.util.List;

/** What the view (and main) can ask the presenter. */
public interface IPresenter {
    void start();

    List<AnimationView> animationViews();

    void setRenderFps(int fps);
    void setScale(int index, int scale);
    void setFps(int index, int fps);
}
