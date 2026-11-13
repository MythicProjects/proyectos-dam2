package model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import contract.Direction;


public class SpritePool {
    private final Map<String, BufferedImage[]> sequence = new HashMap<>();

    // ------------------------------------------------------------------
    // Loading
    // ------------------------------------------------------------------

    // --- loads every idle/walk strip of an entity once
    void loadEntity(String entity) {
        if (contains(entity + "/idle-down")) {
            return;
        }
        for (EntityState state : EntityState.values()) {
            for (Direction direction : Direction.values()) {
                String file = state.name().toLowerCase() + "-" + direction.name().toLowerCase();
                loadStrip(entity + "/" + file, "/sprites/" + entity + "/" + file + ".png");
            }
        }
    }

    // --- one row of square frames
    void loadStrip(String name, String resource){
        requireNew(name);
        BufferedImage probe = SpriteSheet.peek(resource);
        if(probe.getWidth() % probe.getHeight() != 0){
            throw new IllegalArgumentException("Strip frames are not square: " + resource);
        }

        int count = probe.getWidth() / probe.getHeight();
        this.sequence.put(name, new SpriteSheet(resource, count, 1, count).frames());
    }

    void load(String name, String resource, int columns, int rows, int frameCount){
        requireNew(name);
        SpriteSheet sheet = new SpriteSheet(resource, columns, rows, frameCount);
        this.sequence.put(name, sheet.frames());
    }

    void loadMirrored(String name, String sourceName){
        requireNew(name);
        BufferedImage[] source = get(sourceName);
        BufferedImage[] mirrored = new BufferedImage[source.length];
        for (int i = 0; i < source.length; i++) {
            mirrored[i] = mirror(source[i]);
        }

        this.sequence.put(name, mirrored);
    }

    // ------------------------------------------------------------------
    // Queries
    // ------------------------------------------------------------------

    BufferedImage[] get(String name){
        BufferedImage[] frames = this.sequence.get(name);
        if (frames == null){
            throw new IllegalArgumentException("Unknown sequence: " + name);
        }
        return frames.clone();
    }

    boolean contains(String name){
        return this.sequence.containsKey(name);
    }

    // ------------------------------------------------------------------
    // Internals
    // ------------------------------------------------------------------

    private void requireNew(String name){
        if (this.sequence.containsKey(name)){
            throw new IllegalArgumentException("Sequence already loaded: " + name);
        }
    }

    private static BufferedImage mirror(BufferedImage image){
        int width = image.getWidth();
        BufferedImage copy = new BufferedImage(width, image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = copy.createGraphics();
        try{
            AffineTransform file = new AffineTransform(-1, 0, 0, 1, width, 0);
            g.drawImage(image, file, null);
        } finally {
            g.dispose();
        }

        return copy;
    }
}
