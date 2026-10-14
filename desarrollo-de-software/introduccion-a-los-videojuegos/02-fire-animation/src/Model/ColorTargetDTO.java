package Model;

public class ColorTargetDTO {
    private final int temperature;
    private final int color;

    public ColorTargetDTO (int temperature, int color){
        this.temperature = temperature;
        this.color = color;
    }

    public int getTemperature(){
        return temperature;
    }

    public int getColor(){
        return color;
    }
}
