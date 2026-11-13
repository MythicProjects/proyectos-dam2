package contract;

/** What the presenter can ask the view. */
public interface IView {
    void setPresenter(IPresenter presenter);
    void start();                       // shows the window and starts the render thread
    void setRenderFps(int fps);
}
