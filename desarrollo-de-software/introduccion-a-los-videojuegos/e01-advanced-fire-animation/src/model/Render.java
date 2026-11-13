package model;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Render {
    private final FireGenerator fire;
    private volatile Palette palette;
    private volatile BufferedImage front;
    private BufferedImage back;

    public Render(FireGenerator fire, Palette palette){
        this.fire = fire;
        this.palette = palette;
        createImage();
    }

    public void setPalette(Palette palette){ this.palette = palette; }

    private void createImage(){
        front = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_INT_ARGB);
        back = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage renderFrame(){
        if(back.getWidth() != fire.getWidth() || back.getHeight() != fire.getHeight()){ createImage(); }

        // 1. Pintar el fotograma nuevo en la imagen trasera
        int[][] temperatures = fire.getTemperatures();
        Palette p = palette;
        int width = fire.getWidth();

        // ------------------- mejora
        // ------------------- anterior -> iteracion por resolucion y back.setRGB(x, y, palette.getColor(temperatures[y][x]));
        synchronized (back){
            int[] pixels = ((DataBufferInt) back.getRaster().getDataBuffer()).getData();
            for (int y = 0; y < fire.getHeight() - 1; y++){
                int[] row = temperatures[y];
                int offset = y * width;
                for (int x = 0; x < width; x ++){
                    pixels[offset + x] = p.color(row[x]);
                }
            }
        }
        
        // 2. Intercambiar: la trasera pasa a ser la delantera
        BufferedImage temp = front;
        front = back;
        back = temp;

        return front;
    }
    public BufferedImage getFrontImage(){ return front; }
}
