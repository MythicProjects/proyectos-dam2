package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import contract.IModel;

/** The pool of sheets and the animations that use them. Knows nothing about Swing. */
public class Model implements IModel {

    private final SpritePool pool = new SpritePool();
    private final List<SpriteAnimation> animations = new ArrayList<>();

    // ---- Setup: only main calls these, before start() ----

    public void loadSheet(String name, String resource, int columns, int rows, int frameCount) {
        this.pool.load(name, resource, columns, rows, frameCount);
    }

    public void addAnimation(String sheet, int x, int y, int fps, int scale) {
        String name = sheet + " #" + (this.animations.size() + 1);
        this.animations.add(new SpriteAnimation(name, this.pool.get(sheet), x, y, fps, scale));
    }

    // ---- Contract ----

    @Override
    public void start() {
        for (SpriteAnimation animation : this.animations) {
            animation.start();
        }
    }

    @Override public int count() { return this.animations.size(); }
    @Override public String name(int i) { return this.animations.get(i).name(); }
    @Override public BufferedImage frame(int i) { return this.animations.get(i).frame(); }
    @Override public int x(int i) { return this.animations.get(i).x(); }
    @Override public int y(int i) { return this.animations.get(i).y(); }
    @Override public int scale(int i) { return this.animations.get(i).scale(); }
    @Override public int fps(int i) { return this.animations.get(i).fps(); }

    @Override public void setScale(int i, int scale) { this.animations.get(i).setScale(scale); }
    @Override public void setFps(int i, int fps) { this.animations.get(i).setFps(fps); }
}
