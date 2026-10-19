package view;

import presenter.IPresenter;

public interface IView {
    // ------------------------------------------------------------------
    // Presenter
    // ------------------------------------------------------------------
    void setPresenter(IPresenter presenter);
    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------
    void startView();
    // ------------------------------------------------------------------
    // Info
    // ------------------------------------------------------------------
    void showValues(int width, int height, int sparks, int coolPoints, int randomization);
    void showMessage(String message);
}