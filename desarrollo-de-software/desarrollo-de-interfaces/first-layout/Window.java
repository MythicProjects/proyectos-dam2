import javax.swing.*;
import java.awt.*;

public class Window {

    public  void createWindow(){
        // 1. Create window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // al pulsar la X se cierra el programa

        // 2. 
        JPanel contentPane = new JPanel(new GridBagLayout());
        frame.setContentPane(contentPane);

        // 3. Añadir componentes
        JPanel buttonsPanel = createPanel();
        JPanel tablePanel = createPanel();

        contentPane.add(tablePanel, constraints(0, 0, 1, 1, 0.3, 0.5));
        contentPane.add(buttonsPanel, constraints(0, 1, 1, 1, 0.3, 0.5));
        contentPane.add(createCanvas(), constraints(1, 0, 1, 2, 0.7, 1.0));

        buttonsPanel.add(createButton("TestingButton"));
        tablePanel.add(createTable(), constraints(0, 0, 1, 1, 1, 1));

        frame.setSize(800,500);
        frame.setLocationRelativeTo(null);  // centra la ventana en la pantalla
        frame.setVisible(true);
    }

    // Create components
    private JLabel createLabel(String title) {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label.setPreferredSize(new Dimension(0, 0)); // el tamaño lo deciden los weights
        return label;
    }

    private Canvas createCanvas(){
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.blue);
        canvas.setPreferredSize(new Dimension(0,0));
        return canvas;
    }

    private JPanel createPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(e -> System.out.println("Pulsado: " + text));
        return button;
    }

    private JScrollPane createTable() {
    String[] columns = {"Hilo", "Estado", "Iteraciones"};
    Object[][] data = {
            {"Hilo-1", "Ejecutando", 120},
            {"Hilo-2", "Pausado", 85},
            {"Hilo-3", "Terminado", 200}
    };

    JTable table = new JTable(data, columns);
    JScrollPane scroll = new JScrollPane(table);
    scroll.setPreferredSize(new Dimension(0, 0)); // el tamaño lo deciden los weights
    return scroll;
}

    private GridBagConstraints constraints(
        int x, int y, int width, int height, double weightX, double weightY){

            GridBagConstraints c = new GridBagConstraints();

            c.gridx = x;
            c.gridy = y;
            c.gridwidth = width;
            c.gridheight = height;
            c.weightx = weightX;
            c.weighty = weightY;

            c.fill = GridBagConstraints.BOTH; // el componente se estira para llenar su celda (ancho y alto)
            
            c.insets = new Insets(5,5,5,5); // margen de 5 px alrededor (arriba, izquierda, abajo, derecha)

            return c;
        }
}