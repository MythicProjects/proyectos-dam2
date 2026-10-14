package Model;

import java.awt.image.BufferedImage;

public class Model implements IModel{
    private static final int DEFAULT_WIDTH = 1920;
    private static final int DEFAULT_HEIGHT = 1080;
    private final FireGenerator fire;
    private final Render render;

    public Model(){
        fire = new FireGenerator(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        fire.setSparksPercentage(100);
        fire.setCoolPointsPercentage(70);
        fire.setRandomization(50);

        render = new Render(fire, Palette.createPalette());
    }
    
    @Override 
    public void update(){
        fire.setSparkPoints();
        fire.setCoolPoints();
        fire.updateTemperatures();
    }
    @Override 
    public BufferedImage getImage(){
        return render.getImage();
    }
    @Override 
    public void setPalette(Palette palette){
        render.setPalette(palette);
    }
    @Override 
    public void setResolution(int width, int height){
        fire.setResolution(width, height);
    }
    @Override 
    public void setSparksPercentage(double percentage){
        fire.setSparksPercentage(percentage);
    }
    @Override 
    public void setCoolPointsPercentage(double percentage){
        fire.setCoolPointsPercentage(percentage);
    }
    @Override 
    public void setRandomization(int randomization){
        fire.setRandomization(randomization);
    }
}
