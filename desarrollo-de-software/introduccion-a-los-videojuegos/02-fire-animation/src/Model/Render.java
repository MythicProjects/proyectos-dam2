package Model;
import java.awt.image.BufferedImage;

public class Render {
    private final FireGenerator fire;
    private Palette palette;
    private BufferedImage image;

    public Render(FireGenerator fire, Palette palette){
        this.fire = fire;
        this.palette = palette;
        createImage();
    }

    public void setPalette(Palette palette){
        this.palette = palette;
    }

    public BufferedImage getImage(){
        // si cambia resolucion -> generar una nueva imagen
        if(image.getWidth() != fire.getWidth() || image.getHeight() != fire.getHeight()){
                createImage();
        }

        int[][] temperatures = fire.getTemperatures();
        for (int y = fire.getHeight() - 2; y >= 0; y--){
            for(int x = 0; x < fire.getWidth(); x++){
                image.setRGB(x, y, palette.getColor(temperatures[y][x]));
            }
        }

        return image;
    }

    private void createImage(){
        image = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
}
