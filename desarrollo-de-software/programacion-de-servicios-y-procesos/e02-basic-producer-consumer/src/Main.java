import javax.swing.SwingUtilities;

import model.IModel;
import model.Simulation;
import presenter.IPresenter;
import presenter.Presenter;
import view.IView;
import view.View;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IModel model = new Simulation();
            IView view = new View();
            IPresenter presenter = new Presenter(model, view);
            presenter.start();
        });
    }
}
