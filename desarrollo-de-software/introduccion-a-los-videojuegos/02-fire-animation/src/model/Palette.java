package model;

import java.util.ArrayList;
import java.util.List;

import common.PaletteType;

public class Palette {

    public static Palette createWithType(PaletteType type) {
        return switch (type) {
            case FIRE        -> createRedFirePalette();
            case BLUE_FIRE   -> createBlueFirePalette();
            case GREEN_FIRE  -> createGreenFirePalette();
            case PURPLE_FIRE -> createPurpleFirePalette();
            case CYAN_FIRE   -> createCyanFirePalette();
            case RAINBOW_FIRE -> createRainbowFirePalette();
        };
    }

    public static final int MAX_TEMPERATURE = 255;

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private final int[] colors = new int[MAX_TEMPERATURE + 1]; // 0 - 255 --> 256 positions

    public Palette(List<ColorTargetDTO> targets){
        if (targets.size() < 2
            || targets.get(0).temperature() != 0
            || targets.get(targets.size() - 1).temperature() != MAX_TEMPERATURE){
                throw new IllegalArgumentException("Palette must be between 0 and " + MAX_TEMPERATURE);
        }
        
        for (int i = 0; i < targets.size() - 1; i++){
            ColorTargetDTO from = targets.get(i);
            ColorTargetDTO to = targets.get(i + 1);
            
            if (to.temperature() <= from.temperature()){
                throw new IllegalArgumentException("Palette must be between 0 and " + MAX_TEMPERATURE);
            }
            
            int range = to.temperature() - from.temperature();

            // si agrega temperaturas de 1 en 1, se puede hacer mas retro reduciendo pasos?
            for(int t = from.temperature(); t <= to.temperature(); t++){
                double ratio = (double) (t - from.temperature()) / range;
                colors[t] = interpolate(from.color(), to.color(), ratio);
                //colors[t] = to.color());
            }
        }
    }

    public int color(int temperature) {
        return colors[temperature];
    }

    public static Palette createWithColor(int baseColor) {
        int base = baseColor | 0xFF000000; // fuerza el alfa a FF (opaco)

        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0,   BLACK));                         // negro
        targets.add(new ColorTargetDTO(20,  interpolate(BLACK, base, 0.6))); // color oscuro
        targets.add(new ColorTargetDTO(80,  base));                          // color puro
        targets.add(new ColorTargetDTO(150, interpolate(base, WHITE, 0.7))); // color casi blanco
        targets.add(new ColorTargetDTO(255, WHITE));                         // blanco
        return new Palette(targets);
    }


    private static int interpolate(int c1, int c2, double ratio){
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

    private static int mix(int v1, int v2, double ratio){
        return (int) Math.round(v1 + (v2 - v1) * ratio); 
    }
    // Formula de interpolacion lineal // v1 + (v2 - v1) * ratio
    // ratio va de 0 (v1) a 1 (v2)
    // (v2 - v1) -> es la diferencia y se aplica segun ratio hasta ser v2 (ratio = 1)


    // ------------------------------------------------------------------
    // PALETA PERSONALIZADA
    // ------------------------------------------------------------------

    public static Palette createRedFirePalette() {
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0,   0xFF120000)); // negro rojizo
        targets.add(new ColorTargetDTO(20,  0xFF8B0000)); // rojo oscuro
        targets.add(new ColorTargetDTO(60,  0xFFFF1800)); // rojo fuego
        targets.add(new ColorTargetDTO(110, 0xFFFF6500)); // naranja intenso
        targets.add(new ColorTargetDTO(170, 0xFFFFC020)); // amarillo dorado
        targets.add(new ColorTargetDTO(220, 0xFFFFF0A0)); // amarillo pálido
        targets.add(new ColorTargetDTO(255, 0xFFFFFFFF)); // blanco incandescente
        return new Palette(targets);
    }

    public static Palette createBlueFirePalette() {
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0,   0xFF000510)); // negro azulado
        targets.add(new ColorTargetDTO(20,  0xFF001B80)); // azul profundo
        targets.add(new ColorTargetDTO(60,  0xFF005CFF)); // azul eléctrico
        targets.add(new ColorTargetDTO(110, 0xFF00BFFF)); // azul celeste intenso
        targets.add(new ColorTargetDTO(170, 0xFF40F5FF)); // cian luminoso
        targets.add(new ColorTargetDTO(220, 0xFFB8FFFF)); // cian casi blanco
        targets.add(new ColorTargetDTO(255, 0xFFFFFFFF)); // blanco incandescente
        return new Palette(targets);
    }

    public static Palette createGreenFirePalette() {
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0,   0xFF001000)); // negro verdoso
        targets.add(new ColorTargetDTO(20,  0xFF005000)); // verde profundo
        targets.add(new ColorTargetDTO(60,  0xFF00C820)); // verde intenso
        targets.add(new ColorTargetDTO(110, 0xFF50FF00)); // verde neón
        targets.add(new ColorTargetDTO(170, 0xFFB0FF30)); // lima brillante
        targets.add(new ColorTargetDTO(220, 0xFFE8FFB0)); // verde casi blanco
        targets.add(new ColorTargetDTO(255, 0xFFFFFFFF)); // blanco incandescente
        return new Palette(targets);
    }

    public static Palette createPurpleFirePalette() {
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0,   0xFF080014)); // negro violeta
        targets.add(new ColorTargetDTO(20,  0xFF350080)); // violeta profundo
        targets.add(new ColorTargetDTO(60,  0xFF8000FF)); // púrpura eléctrico
        targets.add(new ColorTargetDTO(110, 0xFFDF00FF)); // magenta violeta
        targets.add(new ColorTargetDTO(170, 0xFFFF60FF)); // rosa fluorescente
        targets.add(new ColorTargetDTO(220, 0xFFFFD0FF)); // rosa casi blanco
        targets.add(new ColorTargetDTO(255, 0xFFFFFFFF)); // blanco incandescente
        return new Palette(targets);
    }

    public static Palette createCyanFirePalette() {
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0,   0xFF001014)); // negro petróleo
        targets.add(new ColorTargetDTO(20,  0xFF00505A)); // azul petróleo
        targets.add(new ColorTargetDTO(60,  0xFF00A8B8)); // turquesa intenso
        targets.add(new ColorTargetDTO(110, 0xFF00FFE0)); // turquesa neón
        targets.add(new ColorTargetDTO(170, 0xFF70FFF0)); // aguamarina luminosa
        targets.add(new ColorTargetDTO(220, 0xFFD0FFFA)); // cian casi blanco
        targets.add(new ColorTargetDTO(255, 0xFFFFFFFF)); // blanco incandescente
        return new Palette(targets);
    }

    public static Palette createRainbowFirePalette() {
        List<ColorTargetDTO> targets = new ArrayList<>();
        targets.add(new ColorTargetDTO(0,   0xFF100020)); // negro violeta
        targets.add(new ColorTargetDTO(20,  0xFF7000FF)); // violeta eléctrico
        targets.add(new ColorTargetDTO(55,  0xFFFF00D0)); // magenta intenso
        targets.add(new ColorTargetDTO(90,  0xFFFF0030)); // rojo brillante
        targets.add(new ColorTargetDTO(125, 0xFFFF6500)); // naranja fuego
        targets.add(new ColorTargetDTO(160, 0xFFFFFF00)); // amarillo puro
        targets.add(new ColorTargetDTO(195, 0xFF40FF00)); // verde neón
        targets.add(new ColorTargetDTO(225, 0xFF00EFFF)); // cian eléctrico
        targets.add(new ColorTargetDTO(245, 0xFFB0F8FF)); // azul casi blanco
        targets.add(new ColorTargetDTO(255, 0xFFFFFFFF)); // blanco incandescente
        return new Palette(targets);
    }
}
