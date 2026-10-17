import view.IView;
import view.View;

import javax.swing.SwingUtilities;

import model.IModel;
import model.Model;
import presenter.IPresenter;
import presenter.Presenter;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IModel model = new Model();
            IView view = new View();
            IPresenter presenter = new Presenter(model, view);
            presenter.start();
        });
    }

}