package contract;

public interface IView {
    void setPresenter(IPresenter presenter);
    void start();

    int renderFps();
    void setRenderFps(int fps);

    void playBumpSound();
}
