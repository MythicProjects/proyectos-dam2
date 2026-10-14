import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Model.Model;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Model model = new Model();

        JLabel label = new JLabel(new ImageIcon(model.getImage()));
        JFrame frame = new JFrame("Fire Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(label);
        frame.pack();
        frame.setVisible(true);

        while (true) {
            model.update(); // recalcula imagen
            label.setIcon(new ImageIcon(model.getImage())); // pasa imagen
            label.repaint(); // repinta
            Thread.sleep(16); // 60 unos FPS
        }
    }
}