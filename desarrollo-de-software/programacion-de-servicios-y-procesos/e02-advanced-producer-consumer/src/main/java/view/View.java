package view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import contract.IPresenter;
import contract.IView;
import view.components.DesktopPanel;
import view.components.TopMenuBar;
import view.dto.GameSnapshot;
import view.theme.Theme;

public class View extends JFrame implements IView {
    private final TopMenuBar menuBar;
    private final DesktopPanel desktop;
    private IPresenter presenter;

    public View() {
        super("Resource Factory");
        menuBar = new TopMenuBar();
        desktop = new DesktopPanel();

        setJMenuBar(menuBar);
        menuBar.onThemesSelected(this::applyTheme);
        menuBar.onExit(() -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        menuBar.onAbout(() -> showMessage("Resource Factory"));
        add(desktop, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        applyTheme(Theme.SNES);
    }

    // ------------------------------------------------------------------
    // Wiring
    // ------------------------------------------------------------------
    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = presenter;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                presenter.stop();
            }
        });
        menuBar.onNewGame(presenter::onNewGame);

        menuBar.onPlay(presenter::onPlay);
        menuBar.onPause(presenter::onPause);
        menuBar.onFastForward(presenter::onFastForward);
        desktop.onBuyProduct(presenter::onBuyProduct);
        desktop.onNewProducer(presenter::onNewProducer);
        desktop.onUpgradeProducer(presenter::onUpgradeProducer);
        desktop.onNewProduct(presenter::onNewProduct);
        desktop.onPromo(presenter::onPromo);
    }

    // ------------------------------------------------------------------
    // Display
    // ------------------------------------------------------------------
    @Override
    public void showView() {
        setVisible(true);
    }

    // --- Swing timer (EDT) calls this function
    @Override
    public void render(GameSnapshot snapshot) {
        menuBar.setDay(snapshot.day());
        menuBar.setMoney(snapshot.money());
        desktop.setProducers(snapshot.producers());
        desktop.setConsumers(snapshot.consumers(), snapshot.queue());
        desktop.setProducts(snapshot.products());
        desktop.setActions(snapshot.actions());
        desktop.setPromo(snapshot.promo());
        desktop.setStats(snapshot.stats());
        desktop.setResource(snapshot.stock(), snapshot.stats().resourcePerSec(),
                snapshot.histogram(), snapshot.products());
    }

    @Override
    public void showMessage(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme) {
        menuBar.applyTheme(theme);
        desktop.applyTheme(theme);
        repaint();
    }
}
