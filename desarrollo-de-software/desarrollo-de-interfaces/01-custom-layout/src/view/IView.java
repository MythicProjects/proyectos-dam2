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
    void showValues(int width, int height);
    void showMessage(String message);
}
