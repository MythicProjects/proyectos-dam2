package view;

import presenter.IPresenter;


public interface IView {
    void setPresenter(IPresenter presenter);
    void startView();
    void showValues(int width, int height, int sparks, int coolPoints, int randomization);
    void showMessage(String message);
}