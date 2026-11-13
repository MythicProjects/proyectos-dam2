package presenter;
import model.IModel;
import view.IView;

public class Presenter {
    IModel model;    
    IView view;

    public Presenter(IView view, IModel model){
        this.view = view;
        this.model = model;
    }

    public void start(){
        model.execute();
        view.start();
    }
}
