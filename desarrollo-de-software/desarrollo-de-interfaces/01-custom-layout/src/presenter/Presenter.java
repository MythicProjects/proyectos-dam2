package presenter;
import model.IModel;
import view.IView;
import java.awt.image.BufferedImage;

public class Presenter implements IPresenter {
    // Model
    private final IModel model;

    // View
    private final IView view;
    // --- view settings
    private final int MIN_WIDTH = 16;
    private final int MIN_HEIGHT = 16;

    public Presenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------
    @Override
    public void start() {
        view.setPresenter(this);
        view.showValues(model.getWidth(), model.getHeight());
        view.startView(); // prepare window
        model.play(); // start thread
    }
    // ------------------------------------------------------------------
    // Buttons behaviour
    // ------------------------------------------------------------------
    @Override public void play() { model.play(); }
    @Override public void togglePause() { model.togglePause(); }
    @Override public void stop() { model.stop(); }
    // ------------------------------------------------------------------
    // Get render
    // ------------------------------------------------------------------
    @Override public BufferedImage getImage() { return model.getImage(); }
    // ------------------------------------------------------------------
    // Resolution settings
    // ------------------------------------------------------------------
    @Override public void setResolution(int w, int h) {
        if (w < MIN_WIDTH || h < MIN_HEIGHT) {
            view.showMessage("Resolución mínima: " + MIN_WIDTH + "×" + MIN_HEIGHT);
            return;
        }
        model.setResolution(w, h);
        view.showValues(model.getWidth(), model.getHeight());  // keep every DataPanel up to date
    }

}
