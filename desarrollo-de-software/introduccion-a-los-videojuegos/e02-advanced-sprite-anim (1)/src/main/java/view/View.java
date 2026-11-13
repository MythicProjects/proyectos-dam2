package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import contract.IPresenter;
import contract.IView;

/** The window, its render thread and its sounds. Knows the presenter only through its interface. */
public class View extends JFrame implements IView, Runnable {

    private static final int DEFAULT_RENDER_FPS = 60;
    private static final int DEFAULT_SCALE = 3;

    private final Viewer viewer;
    private final ControlPanel controls;
    private final AudioPlayer audio;
    private IPresenter presenter;
    private volatile int renderFps = DEFAULT_RENDER_FPS;

    public View() {
        super("Sprite Renderer");
        this.viewer = new Viewer(DEFAULT_SCALE);
        this.controls = new ControlPanel();
        this.audio = new AudioPlayer();
        this.audio.startMusic();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(this.viewer, BorderLayout.CENTER);
        add(this.controls, BorderLayout.EAST);
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void start() {
        if (this.presenter == null) {
            throw new IllegalStateException("start() called before setPresenter()");
        }
        SwingUtilities.invokeLater(() -> {
            KeyHandler keys = new KeyHandler(this.presenter);
            this.viewer.setFocusable(true);
            this.viewer.addKeyListener(keys);
            this.viewer.addFocusListener(keys);

            this.viewer.setBackgroundImage(this.presenter.background());
            buildControls();
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
            this.viewer.requestFocusInWindow();   // arrows go to the scene from the start
        });

        Thread thread = new Thread(this, "Render");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public int renderFps() {
        return this.renderFps;
    }

    @Override
    public void setRenderFps(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("Render fps must be positive: " + fps);
        }
        this.renderFps = fps;
    }

    @Override
    public void playBumpSound() {
        this.audio.playBump();
    }

    /** Render loop: ask the presenter what to draw, hand it to the canvas, wait, repeat. */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long frameStart = System.nanoTime();
            this.viewer.setViews(this.presenter.animationViews());

            long frameNanos = 1_000_000_000L / this.renderFps;
            long sleepMillis = (frameNanos - (System.nanoTime() - frameStart)) / 1_000_000;
            if (sleepMillis > 0) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    /** Sliders need the presenter's limits, so they are built once it exists. */
    private void buildControls() {
        int min = this.presenter.minFps();
        int max = this.presenter.maxFps();

        this.controls.addSlider("Render", min, max, this.renderFps, "fps",
                fps -> this.presenter.setRenderSpeed(fps));

        this.controls.addSlider("Player anim", min, max, this.presenter.playerSpeed(), "fps",
                fps -> this.presenter.setPlayerSpeed(fps));

        this.controls.addSlider("Player walk", this.presenter.minMoveSpeed(), this.presenter.maxMoveSpeed(),
                this.presenter.playerMoveSpeed(), "px/s",
                speed -> this.presenter.setPlayerMoveSpeed(speed));
    }
}