package main;

import javax.swing.SwingUtilities;

import contract.IModel;
import contract.IPresenter;
import contract.IView;
import model.Model;
import presenter.Presenter;
import view.View;

public class Main {
    // ------------------------------------------------------------------
    // Entry point
    // ------------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            IModel model = new Model();
            IView view = new View();
            IPresenter presenter = new Presenter(model, view);
            presenter.start();
        });
    }
}
