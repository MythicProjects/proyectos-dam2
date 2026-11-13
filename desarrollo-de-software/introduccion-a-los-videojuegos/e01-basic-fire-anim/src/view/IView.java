package view;

import presenter.IPresenter;

public interface IView {
    void setPresenter(IPresenter presenter);
    void start();
}
