package model;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/** Every animation's frames, loaded once and shared by all its instances. */
final class SpritePool {

    private final Map<String, BufferedImage[]> animations = new HashMap<>();

    void load(String name, String resource, int columns, int rows, int frameCount) {
        this.animations.put(name, SpriteSheet.cut(resource, columns, rows, frameCount));
    }

    BufferedImage[] get(String name) {
        BufferedImage[] frames = this.animations.get(name);
        if (frames == null) {
            throw new IllegalArgumentException("Unknown animation: " + name);
        }
        return frames;
    }
}
