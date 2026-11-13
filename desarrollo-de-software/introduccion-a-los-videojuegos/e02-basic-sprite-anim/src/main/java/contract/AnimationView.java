package contract;

import java.awt.image.BufferedImage;

/** Everything the view needs to draw one animation. Immutable. */
public record AnimationView(String name, BufferedImage image, int x, int y, int scale, int fps) {}
