package src.model;

public class ColorTargetDTO {
    private final int temperature;
    private final int alpha;
    private final int red;
    private final int green;
    private final int blue;

    public ColorTargetDTO (int temperature, int alpha, int red, int green, int blue){
        this.temperature = temperature;
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getTemperature() { return temperature; }
    public int getAlpha() { return alpha; }
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
}