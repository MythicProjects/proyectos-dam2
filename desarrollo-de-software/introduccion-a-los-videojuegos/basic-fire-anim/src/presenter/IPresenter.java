
import java.awt.image.BufferedImage;

public interface IPresenter {

    void start();

    BufferedImage getImage(); // devuelve imagen -> BufferedImage, clase que permite obtener la imagen / pixeles
}