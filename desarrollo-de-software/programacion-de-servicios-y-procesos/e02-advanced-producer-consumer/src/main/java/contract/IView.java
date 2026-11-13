package contract;

import view.dto.GameSnapshot;
import view.theme.Themed;

public interface IView extends Themed {
    // ------------------------------------------------------------------
    // Wiring
    // ------------------------------------------------------------------
    void setPresenter(IPresenter presenter);

    // ------------------------------------------------------------------
    // Display
    // ------------------------------------------------------------------
    void showView();
    void render(GameSnapshot snapshot);
    void showMessage(String text);
}
