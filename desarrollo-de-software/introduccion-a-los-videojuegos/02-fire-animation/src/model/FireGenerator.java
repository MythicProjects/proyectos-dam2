package model;

import java.util.Arrays;
import java.util.SplittableRandom;

public class FireGenerator {
    private int width;
    private int height;
    private int[][] temperatures;

    private volatile double sparksPercentage;
    private volatile double coolPointPercentage;
    private volatile int randomization; 
    private volatile double cooling = 0.1; // enfriamiento por fila: controla la altura

    private final SplittableRandom random = new SplittableRandom(); // en vez de Random

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
    public boolean isCold(){
        for (int[] row : temperatures){
            for (int t : row){
                if ( t > 0) return false;
            }
        }

        return true;
    }
    public  void updateTemperatures(){
        double variation = randomization / 100.0;
        int lastX = width - 1;

        for(int y = height - 2; y >= 0; y--){
            int[] row = temperatures [y];
            int[] below = temperatures [y + 1];

            for (int x = 0; x <= lastX; x++){
                int sum = 
                    row[x] + below[x == 0 ? 0 : x - 1] + // left
                    below[x] + // center
                    below[x == lastX ? lastX : x + 1]; // right

                double avg = sum / 4;
                double noise = (random.nextDouble() * 2 - 1) * variation;
                int t = (int) Math.round(avg * (1 + noise) - cooling);

                row[x] = t < 0 ? 0 : Math.min(t, Palette.MAX_TEMPERATURE);
            }
        }
    }
    // --------------------------- old updateTemperatures - facil
    /* 
    public void updateTemperatures(){
        double maxNoise = Math.max(0, randomization - 10);
        
        for (int y = height - 2; y >= 0; y--){
            for(int x = 0; x < width; x++){
                double left = temperatures[y + 1][Math.max(x - 1, 0)]; // limitar izquierda
                double center = temperatures[y + 1][x];
                double right = temperatures[y + 1][Math.min(x + 1, width - 1)]; // limitar derecha
                double self = temperatures[y][x];

                //int t = (self + left + center + right) / 4 - random.nextInt(randomization + 1); // resta de 0 a random para dar variedad
                double t = (self + left + center + right) / 3.9 - BASE_COOLING - random.nextDouble(maxNoise + 1);
                t = Math.min(Palette.MAX_TEMPERATURE, t);

                temperatures[y][x] = (int) Math.max(t, 0);
            }
        }
    }
    // fila actual> self
    // fila anterior > y + 1 (for va de mayor a menor height)
    // posiciones: left: x -1, center: x + 0, right: x + 1
    // cuatro valores> hacer media y randomizar con alguna formula
    // */

    public int[][] getTemperatures(){ return temperatures; }
    public void clearTemperatures() { temperatures = new int[height][width]; }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getCoolPointsPercentage() { return coolPointPercentage; }
    public int getRandomization() { return randomization; }

    public void setSparksPercentage(double p){ this.sparksPercentage = p; }
    public void setCoolPointsPercentage(double p){ this.coolPointPercentage = p; }
    public void setRandomization(int r){ this.randomization = r; }
    public void setCooling(double c){ this.cooling = c;  System.out.println("cooling recibido: " + cooling);}

    public void clearBaseRow() { Arrays.fill(temperatures[height - 1], 0); }

}
