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

    public FireGenerator(int width, int height){ setResolution(width, height); }

    public void setResolution(int width, int height){
        this.width = width;
        this.height = height;
        this.temperatures = new int[height][width];
    }
    // ------------------------------------------------------------------
    // Setting points
    // ------------------------------------------------------------------
    // ------------------- Spark Points
    public void setSparkPoints(){
        int base = height - 1;
        for (int x = 0; x < width; x++){
            if(random.nextDouble() * 100 < sparksPercentage){ 
                temperatures[base][x] = Palette.MAX_TEMPERATURE; 
            }
        }
    }
    // ------------------- Cool Points
    public void setCoolPoints(){
        int base = height - 1; // recordar que height es el total
        for (int x = 0; x < width; x++){
            if(random.nextDouble() * 100 < coolPointPercentage){
                temperatures[base][x] = 0;
            }
        }
    }
    // ------------------------------------------------------------------
    // Calculate temperatures
    //      - Old sistem: get 4 temperatures, divide by 4 and return it to the iteration
    // ------------------------------------------------------------------
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
    // ------------------------------------------------------------------
    // Temperatures
    // ------------------------------------------------------------------
    // ------------------- check if is cold
    public boolean isCold(){
        for (int[] row : temperatures){
            for (int t : row){
                if ( t > 0) return false;
            }
        }
        return true;
    }
    // ------------------- clear temperatures
    public void clearTemperatures() { temperatures = new int[height][width]; }
    public void clearBaseRow() { Arrays.fill(temperatures[height - 1], 0); }
    // ------------------- get temperatures
    public int[][] getTemperatures(){ return temperatures; }
    // ------------------------------------------------------------------
    // Get Resolution
    // ------------------------------------------------------------------
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    // ------------------------------------------------------------------
    // Get Fire settings
    // ------------------------------------------------------------------
    public double getSparksPercentage() { return sparksPercentage; }
    public double getCoolPointsPercentage() { return coolPointPercentage; }
    public int getRandomization() { return randomization; }
    public double getCooling() { return cooling; }
    // ------------------------------------------------------------------
    // Fire settings
    // ------------------------------------------------------------------
    public void setSparksPercentage(double p){ this.sparksPercentage = p; }
    public void setCoolPointsPercentage(double p){ this.coolPointPercentage = p; }
    public void setRandomization(int r){ this.randomization = r; }
    public void setCooling(double c){ this.cooling = c; }
}
