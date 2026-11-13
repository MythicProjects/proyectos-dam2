package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import contract.AnimationView;
import contract.IPresenter;
import contract.IView;

/** The window: canvas on the left, sliders on the right, and the render thread. */
public class View extends JFrame implements IView, Runnable {

    private final Canvas canvas = new Canvas(820, 460);
    private final ControlPanel controls = new ControlPanel();
    private IPresenter presenter;
    private volatile int renderFps = 30;

    public View() {
        super("Sprite Animation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(this.canvas, BorderLayout.CENTER);
        JScrollPane scroll = new JScrollPane(this.controls);       // 21 sliders: they need scrolling
        scroll.setPreferredSize(new Dimension(240, 460));         // as tall as the canvas
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.EAST);
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> {      // Swing components are built on Swing's thread
            buildControls();
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        });
        Thread render = new Thread(this, "Render");
        render.setDaemon(true);
        render.start();
    }

    @Override
    public void setRenderFps(int fps) {
        this.renderFps = Math.max(1, fps);
    }

    /** Render loop: ask what to draw, hand it to the canvas, wait, repeat. */
    @Override
    public void run() {
        while (true) {
            this.canvas.setViews(this.presenter.animationViews());
            try {
                Thread.sleep(1000 / this.renderFps);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void buildControls() {
        this.controls.addTitle("Render");
        this.controls.addSlider("fps", 1, 60, this.renderFps, fps -> this.presenter.setRenderFps(fps));

        List<AnimationView> views = this.presenter.animationViews();
        for (int i = 0; i < views.size(); i++) {
            int index = i;                      // lambdas need an effectively final copy
            AnimationView view = views.get(i);
            this.controls.addTitle(view.name());
            this.controls.addSlider("size", 1, 8, view.scale(), s -> this.presenter.setScale(index, s));
            this.controls.addSlider("fps", 1, 30, view.fps(), f -> this.presenter.setFps(index, f));
        }
    }
}
