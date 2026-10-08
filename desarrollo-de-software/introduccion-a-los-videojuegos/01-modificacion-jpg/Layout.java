import javax.swing.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Layout extends JFrame {
    public Layout() {
        setLayout(new GridLayout(1, 4));

        String resoure = "/img/foto.jpg";

        try{
            BufferedImage img = ImageIO.read(getClass().getResource(resoure));
            BufferedImage imgDesatured = ImageIO.read(getClass().getResource(resoure));
            BufferedImage imgColorized = ImageIO.read(getClass().getResource(resoure));
            BufferedImage imgInverted = ImageIO.read(getClass().getResource(resoure));

            desaturateIMG(imgDesatured); // Desaturar
            colorizedIMG(imgColorized, new Color(128,255,212)); // Colorizar
            invertIMG(imgInverted); // Invertir colores

            add(new JLabel(resizeIMG(img)));
            add(new JLabel(resizeIMG(imgDesatured)));
            add(new JLabel(resizeIMG(imgColorized)));
            add(new JLabel(resizeIMG(imgInverted)));
        }
        catch(IOException e){
            e.printStackTrace();
        }

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void desaturateIMG(BufferedImage img){
        for (int y = 0; y < img.getHeight(); y++){
            for (int x = 0; x < img.getWidth(); x++){
                int rgb = img.getRGB(x, y);

                int r = (rgb >> 16 & 0xFF);
                int g = (rgb >> 8 & 0xFF);
                int b = (rgb & 0xFF);

                int grey = (r + g + b) / 3;

                img.setRGB(x, y, (grey << 16 | grey << 8 | grey));
            }
        }
    }

    private void colorizedIMG(BufferedImage img, Color color){
        for (int y = 0; y < img.getHeight(); y++){
            for (int x = 0; x < img.getWidth(); x++){
                int rgb = img.getRGB(x, y);

                int r = (rgb >> 16 & 0xFF);
                int g = (rgb >> 8 & 0xFF);
                int b = (rgb & 0xFF);

                int grey = (r + g + b) / 3;

                int red = grey * color.getRed() / 255;
                int green = grey * color.getGreen() / 255;
                int blue = grey * color.getBlue() / 255;

                img.setRGB(x, y, (red << 16 | green << 8 | blue));
            }
        }
    }

    private void invertIMG(BufferedImage img){
        for (int y = 0; y < img.getHeight(); y++){
            for (int x = 0; x < img.getWidth(); x++){
                int rgb = img.getRGB(x, y);

                int r = 255 - (rgb >> 16 & 0xFF);
                int g = 255 - (rgb >> 8 & 0xFF);
                int b = 255 - (rgb & 0xFF);

                img.setRGB(x, y, (r << 16 | g << 8 | b));
            }
        }
    }

    private ImageIcon resizeIMG(BufferedImage img){
        double factor = Math.min(
            (double) 1000 / img.getWidth(), 
            (double) 500 / img.getHeight());
        
        if(factor >= 1){
            return new ImageIcon(img);
        }

        int width = (int) (img.getWidth()*factor);
        int height = (int) (img.getHeight()*factor);

        return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
