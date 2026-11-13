package presenter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import contract.AnimationView;
import contract.Direction;
import contract.IModel;
import contract.IModelListener;
import contract.IPresenter;
import contract.IView;

public class Presenter implements IPresenter, IModelListener {

    private final IModel model;
    private final IView view;

    private int minFps = 1;
    private int maxFps = 120;
    private int minMoveSpeed = 16;
    private int maxMoveSpeed = 160;

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------

    // --- receives model and view, never creates them, and wires itself to both
    public Presenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
        model.setListener(this);
        view.setPresenter(this);
    }

    // ------------------------------------------------------------------
    // Configuration (not in the contract)
    // ------------------------------------------------------------------

    // --- only main calls these, before start()
    public void setFpsLimits(int min, int max) {
        if (min <= 0 || max < min) {
            throw new IllegalArgumentException("Invalid fps limits: " + min + ".." + max);
        }
        this.minFps = min;
        this.maxFps = max;
    }

    public void setMoveSpeedLimits(int min, int max) {
        if (min <= 0 || max < min) {
            throw new IllegalArgumentException("Invalid speed limits: " + min + ".." + max);
        }
        this.minMoveSpeed = min;
        this.maxMoveSpeed = max;
    }

    // ------------------------------------------------------------------
    // Lifecycle
    // ------------------------------------------------------------------

    @Override
    public void start() {
        this.model.start();
        this.view.start();
    }

    // ------------------------------------------------------------------
    // Drawing
    // ------------------------------------------------------------------

    @Override
    public BufferedImage background() {
        return this.model.background();
    }

    // --- render thread calls this once per frame
    @Override
    public List<AnimationView> animationViews() {
        int count = this.model.entityCount();
        List<AnimationView> views = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            views.add(new AnimationView(
                    this.model.frame(i),
                    this.model.x(i),
                    this.model.y(i),
                    this.model.name(i),
                    this.model.fps(i)));
        }
        return List.copyOf(views);
    }

    @Override
    public int entityCount() {
        return this.model.entityCount();
    }

    // ------------------------------------------------------------------
    // Speed limits
    // ------------------------------------------------------------------

    @Override public int minFps() { return this.minFps; }
    @Override public int maxFps() { return this.maxFps; }
    @Override public int minMoveSpeed() { return this.minMoveSpeed; }
    @Override public int maxMoveSpeed() { return this.maxMoveSpeed; }

    // ------------------------------------------------------------------
    // Speeds
    // ------------------------------------------------------------------

    // --- Swing thread calls these (sliders); values are clamped to the limits
    @Override
    public void setRenderSpeed(int fps) {
        this.view.setRenderFps(clamp(fps, this.minFps, this.maxFps));
    }

    @Override
    public int playerSpeed() {
        return this.model.playerFps();
    }

    @Override
    public void setPlayerSpeed(int fps) {
        this.model.setPlayerFps(clamp(fps, this.minFps, this.maxFps));
    }

    @Override
    public int playerMoveSpeed() {
        return this.model.playerMoveSpeed();
    }

    @Override
    public void setPlayerMoveSpeed(int pixelsPerSecond) {
        this.model.setPlayerMoveSpeed(clamp(pixelsPerSecond, this.minMoveSpeed, this.maxMoveSpeed));
    }

    // ------------------------------------------------------------------
    // Player input
    // ------------------------------------------------------------------

    // --- Swing thread calls these (keys)
    @Override
    public void movePlayer(Direction direction) {
        this.model.movePlayer(direction);
    }

    @Override
    public void stopPlayer() {
        this.model.stopPlayer();
    }

    // ------------------------------------------------------------------
    // Model events
    // ------------------------------------------------------------------

    // --- player thread calls this when it bumps into something
    @Override
    public void onPlayerBlocked() {
        this.view.playBumpSound();
    }

    // ------------------------------------------------------------------
    // Internals
    // ------------------------------------------------------------------

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
