package Model;

import java.util.ArrayList;
import java.util.List;

public class Palette {
    public static final int MAX_TEMPERATURE = 255;
    private final int[] colors = new int[MAX_TEMPERATURE + 1]; // 0 - 255 --> 256 positions

    public Palette(List<ColorTargetDTO> targets){
        for (int i = 0; i < targets.size() - 1; i++){
            ColorTargetDTO from = targets.get(i);
            ColorTargetDTO to = targets.get(i + 1);
            int range = to.getTemperature() - from.getTemperature();

            // si agrega temperaturas de 1 en 1, se puede hacer mas retro reduciendo pasos?
            for(int t = from.getTemperature(); t <= to.getTemperature(); t++){
                double ratio = (double) (t - from.getTemperature()) / range;
                colors[t] = interpolate(from.getColor(), to.getColor(), ratio);
                //colors[t] = to.getColor());
            }
        }
    }

    public int getColor(int temperature) {
        return colors[temperature];
    }

    private int interpolate(int c1, int c2, double ratio){
        int a = mix((c1 >> 24) & 0xFF, (c2 >> 24) & 0xFF, ratio);
        int r = mix((c1 >> 16) & 0xFF, (c2 >> 16) & 0xFF, ratio);
        int g = mix((c1 >> 8) & 0xFF, (c2 >> 8) & 0xFF, ratio);
        int b = mix(c1& 0xFF, c2 & 0xFF, ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    // & 0xFF crea una mascara AND de 8 bits
    // 0x prefijo JAVA -> "lo que sigue es hexadecimal"
    // int siempre 32 bits, FF son 8 bits
    // (c1 >> 24) -> c1 (int) tiene 32 bits coloca el alpha en este caso justo donde la mascara (8 bits iniciales)

    private int mix(int v1, int v2, double ratio){
        return (int) Math.round(v1 + (v2 - v1) * ratio); 
    }
    // Formula de interpolacion lineal // v1 + (v2 - v1) * ratio
    // ratio va de 0 (v1) a 1 (v2)
    // (v2 - v1) -> es la diferencia y se aplica segun ratio hasta ser v2 (ratio = 1)

    public static Palette createPalette(){
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0, 0xFF000000));   // negro
        //targets.add(new ColorTargetDTO(70, 0xFF8A0A00));  // rojo oscuro
        targets.add(new ColorTargetDTO(130, 0xFFE03A00)); // rojo
        //targets.add(new ColorTargetDTO(180, 0xFFFF8C00)); // naranja
        targets.add(new ColorTargetDTO(225, 0xFFFFD020)); // amarillo
        targets.add(new ColorTargetDTO(255, 0xFFFFFFE0)); // blanco cálido
        return new Palette(targets);
    }
}
