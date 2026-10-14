package Model;

import java.util.Random;

public class FireGenerator {
    private int width;
    private int height;
    private int[][] temperatures;

    private double sparksPercentage;
    private double coolPointPercentage;
    private int randomization; // agregacion propia
    private static final double KEEP_HEAT = 0.9;

    private final Random random = new Random();

    public FireGenerator(int width, int height){
        setResolution(width, height);
    }

    public void setResolution(int width, int height){
        this.width = width;
        this.height = height;
        this.temperatures = new int[height][width];
    }

    public void setSparkPoints(){
        int base = height - 1; // recordar que height es el total
        for (int x = 0; x < width; x++){
            if(random.nextDouble() * 100 < sparksPercentage){ // de 0 a 1 x 100 -> compara con el porcentaje
                temperatures[base][x] = Palette.MAX_TEMPERATURE; // Temperatura maxima asignada en la paleta
            }
        }
    }

    public void setCoolPoints(){
        int base = height - 1; // recordar que height es el total
        for (int x = 0; x < width; x++){
            if(random.nextDouble() * 100 < coolPointPercentage){
                temperatures[base][x] = 0;
            }
        }
    }

    public void updateTemperatures(){
        for (int y = height - 2; y >= 0; y--){

            double h = 1.0 - (double) y / (height - 1); // porcentaje de altura

            for(int x = 0; x < width; x++){
                int left = temperatures[y + 1][Math.max(x - 1, 0)]; // limitar izquierda
                int center = temperatures[y + 1][x];
                int right = temperatures[y + 1][Math.min(x + 1, width - 1)]; // limitar derecha
                int self = temperatures[y][x];

                //int t = (self + left + center + right) / 4 - random.nextInt(randomization + 1); // resta de 0 a random para dar variedad
                int t = (self + left + center + right) / 4;

                // posiblidad de continuar calor
                if (random.nextDouble() < (1 - h) * KEEP_HEAT){
                    t = Math.max(t, center);
                }

                // posibilidad de enfriar - segun altura
                if (random.nextDouble() < h){
                    t -= random.nextInt(randomization + 1);
                }

                temperatures[y][x] = Math.max(t, 0);
            }
        }
    }
    // fila actual> self
    // fila anterior > y + 1 (for va de mayor a menor height)
    // posiciones: left: x -1, center: x + 0, right: x + 1
    // cuatro valores> hacer media y randomizar con alguna formula

    public int[][] getTemperatures(){
        return temperatures;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public void setSparksPercentage(double sparksPercentage){
        this.sparksPercentage = sparksPercentage;
    }
    public void setCoolPointsPercentage(double coolPointPercentage){
        this.coolPointPercentage = coolPointPercentage;
    }

    public void setRandomization(int randomization){
        this.randomization = randomization;
    }
}
