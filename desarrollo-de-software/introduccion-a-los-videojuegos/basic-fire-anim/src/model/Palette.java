package src.model;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class Palette {
    // 0 a 255 -> para suavizar
    private final int[] colors = new int[256];

    public Palette(List<ColorTargetDTO> targets){
        // 1. Recorrer targets de la futura paleta
        for(int i = 0; i < targets.size() - 1; i++){
            ColorTargetDTO from = targets.get(i);
            ColorTargetDTO to = targets.get(i + 1);

            // Rango = B - A 
            // Hablamos de temperatura
            int range = to.getTemperature() - from.getTemperature();

            for(int t = from.getTemperature(); t <= to.getTemperature(); t++){
                double ratio = (double) (t - from.getTemperature()) / range; // valor entre 0 y 1

                int alpha = mix(from.getAlpha(), to.getAlpha(), ratio);
                int red = mix(from.getRed(), to.getRed(), ratio);
                int green = mix(from.getGreen(), to.getGreen(), ratio);
                int blue = mix(from.getBlue(), to.getBlue(), ratio);

                colors[t] = new Color(red, green, blue, alpha).getRGB();
            }
        }
    }
    public int color(int temperatura){
        return colors[temperatura];
    }

    // Ratio = valor entre 0 y 1
    // si es 0 = V1
    // si es 1 = v2
    private int mix (int v1, int v2, double ratio){
        return (int) Math.round(v1 + (v2 - v1) * ratio);
    }

    public static Palette createPalette(){
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0, 0, 0, 0, 0)); //negro
        targets.add(new ColorTargetDTO( 20, 255,  224,   58,   0)); // rojo
        targets.add(new ColorTargetDTO( 80, 255,  255,  208,  32)); // amarillo
        targets.add(new ColorTargetDTO(255, 255, 255, 255, 255)); // blanco

        return new Palette(targets);
    }
}
