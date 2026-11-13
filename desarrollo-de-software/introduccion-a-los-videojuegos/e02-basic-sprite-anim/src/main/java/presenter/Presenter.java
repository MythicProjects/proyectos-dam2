package presenter;

import java.util.ArrayList;
import java.util.List;

import contract.AnimationView;
import contract.IModel;
import contract.IPresenter;
import contract.IView;

/** The only piece that talks to both: passes data to the view and orders to the model. */
public class Presenter implements IPresenter {

    private final IModel model;
    private final IView view;

    public Presenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        this.model.start();
        this.view.start();
    }

    @Override
    public List<AnimationView> animationViews() {
        List<AnimationView> views = new ArrayList<>();
        for (int i = 0; i < this.model.count(); i++) {
            views.add(new AnimationView(this.model.name(i), this.model.frame(i),
                    this.model.x(i), this.model.y(i), this.model.scale(i), this.model.fps(i)));
        }
        return views;
    }

    @Override public void setRenderFps(int fps) { this.view.setRenderFps(fps); }
    @Override public void setScale(int index, int scale) { this.model.setScale(index, scale); }
    @Override public void setFps(int index, int fps) { this.model.setFps(index, fps); }
}
