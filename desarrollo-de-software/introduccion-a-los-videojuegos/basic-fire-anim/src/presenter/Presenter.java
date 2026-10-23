import java.awt.image.BufferedImage;

import src.model.IModel;
import src.view.IView;

public class Presenter implements IPresenter {
    private final IModel model;
    
    private final IView view;
    private final int frameMS = 33; // ~ 30 FPS;

    public Presenter(IModel model, IView view){
        this.model = model;
        this.view = view;
    }

    @Override 
    public void start(){
        model.start();
        // view.start();
    }

    @Override 
    public BufferedImage getImage() { return model.getImage(); }
}

