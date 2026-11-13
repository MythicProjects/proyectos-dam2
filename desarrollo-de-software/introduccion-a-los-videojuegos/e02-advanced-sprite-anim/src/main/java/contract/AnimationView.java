package contract;

import java.awt.image.BufferedImage;

public record AnimationView(BufferedImage image, int x, int y, String name, int fps) {}
