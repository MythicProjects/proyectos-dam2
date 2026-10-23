import model.IModel;
import model.Model;
import presenter.Presenter;
import view.IView;
import view.View;

public class Main {
    public static void main(String[] args) {
        IView view = new View();
        IModel model = new Model();

        Presenter presenter = new Presenter(view, model);
        presenter.start(); // or whatever method starts the app
    }
}