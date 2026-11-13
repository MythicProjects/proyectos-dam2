package presenter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

import model.Config;
import model.IModel;
import model.Resource;
import view.IView;
import view.dto.ResourceRow;
import view.dto.Settings;

public class Presenter implements IPresenter {
    private static final int REFRESH_MS = 200;

    private final IModel model;
    private final IView view;
    private final Timer timer;   // runs on the EDT

    public Presenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
        this.timer = new Timer(REFRESH_MS, e -> refresh());
        view.setPresenter(this);
    }

    @Override
    public void start() {
        view.showView();
    }

    @Override
    public void onStart() {
        Settings s = view.getSettings();

        String error = validate(s);
        if (error != null) {
            view.showError(error);
            return;
        }

        model.start(toConfig(s));
        view.setRunning(true);
        timer.start();
    }

    @Override
    public void onStop() {
        timer.stop();
        model.stop();
        refresh();   // last values
        view.setRunning(false);
    }

    // Model -> View
    private void refresh() {
        List<ResourceRow> rows = new ArrayList<>();
        for (Resource r : model.getResources()) {
            rows.add(new ResourceRow(r.getId(), r.getStock(), r.getExpectedStock(),
                    r.getProduced(), r.getConsumed()));
        }
        view.showResources(rows);
    }

    // View -> Model
    private static Config toConfig(Settings s) {
        return new Config(
                s.producers(), s.consumers(), s.resourceTypes(),
                s.minProduceMs(), s.maxProduceMs(),
                s.minConsumeMs(), s.maxConsumeMs(),
                s.produceAmount(), s.consumeAmount(),
                s.minStock(), s.maxStock(),
                s.stockControl(), s.synchronize(), s.waitNotify());
    }

    private static String validate(Settings s) {
        if (s.minProduceMs() > s.maxProduceMs()) return "Min produce time is greater than max.";
        if (s.minConsumeMs() > s.maxConsumeMs()) return "Min consume time is greater than max.";
        if (s.minStock() > s.maxStock()) return "Min stock is greater than max stock.";
        return null;
    }
}
