package src.model;

import java.util.Random;

public class Fire {
    private final int sparksPercentage = 40;
    private final int coolPointsPercentage = 30;
    private final int randomizer = 1;

    private final int width;
    private final int height;
    private final int[][] temperatures; 

    private final Random random = new Random();


    public Fire(int width, int height){
        this.width = width;
        this.height = height;
        this.temperatures = new int[height][width];
    }

    public void sparksGeneration(){
        int base = height - 1;
        for (int x = 0; x < width; x++){
            if (random.nextInt(100) < sparksPercentage){
                temperatures[base][x] = 255;
            }
        }
    }
    public void coolPointsGeneration(){
        int base = height - 1;
        for (int x = 0; x < width; x++){
            if (random.nextInt(100) < coolPointsPercentage){
                temperatures[base][x] = 0;
            }
        }
    }

    public void updateFlames(){
        for(int y = height - 2; y >= 0; y--){
            for(int x = 0; x < width; x++){

                //              [actual] = [y][x]
                //  [izquierda] [centro] [derecha] --- [actual - 1X] -- [y - 1] [...]
                int actual = temperatures[y][x];
                int left = temperatures[y - 1][x - 1];
                int center = temperatures[y - 1][x];
                int right = temperatures[y - 1][x + 1];

                int t = (actual + left + right + center) / 4;

                temperatures[y][x] = Math.max(t, 0);
            }
        }
    }

    public int[][] getTemperature(){ return temperatures; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
