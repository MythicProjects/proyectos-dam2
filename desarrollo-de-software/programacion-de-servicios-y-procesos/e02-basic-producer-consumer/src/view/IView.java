package view;

import java.util.List;

import presenter.IPresenter;
import view.dto.ResourceRow;
import view.dto.Settings;

public interface IView {
    void setPresenter(IPresenter presenter);
    void showView();
    Settings getSettings();
    void showResources(List<ResourceRow> rows);
    void setRunning(boolean running);
    void showError(String message);
}
