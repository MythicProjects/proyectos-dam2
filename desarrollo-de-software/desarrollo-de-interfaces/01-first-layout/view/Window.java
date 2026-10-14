package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.KeyEvent;

import java.awt.*;

public class Window {
    private GraphicElements element;

    public Window(GraphicElements elements){
        this.element = elements;
    }

    public void createWindow(){
        // 1. Create window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // al pulsar la X se cierra el programa

        // 2.
        JPanel contentPane = new JPanel(new GridBagLayout());
        frame.setContentPane(contentPane);

        // 3. Añadir componentes
        JPanel buttonsPanel = element.createPanel();
        JPanel tablePanel = element.createPanel();
        Canvas canvasPanel = element.createCanvas();
        element.flexible(buttonsPanel);   // tamaño preferido 0 x 0: mantiene el 30 % exacto
        element.flexible(tablePanel);

        contentPane.add(tablePanel, constraints(0, 0, 1, 1, 0.3, 0.5));
        contentPane.add(buttonsPanel, constraints(0, 1, 1, 1, 0.3, 0.5));
        contentPane.add(canvasPanel, constraints(1, 0, 1, 2, 0.7, 1.0));

        // 4. Añadir subcomponentes --------------------------------------

        // 4.1 Barra de menús: va en el frame, no en un panel
        JMenuItem openItem = element.createMenuItem("Abrir", KeyEvent.VK_O, e -> System.out.println("Abrir"));
        JMenuItem exitItem = element.createMenuItem("Salir", KeyEvent.VK_Q, e -> frame.dispose());
        JMenu fileMenu = element.createMenu("Archivo", KeyEvent.VK_A, openItem, null, exitItem);   // null = separador
        JMenuBar menuBar = element.createMenuBar(fileMenu);
        frame.setJMenuBar(menuBar);

        // 4.2 Recuadro superior: pestañas (un tipo de elemento en cada una) y barra de estado
        tablePanel.setLayout(new BorderLayout());       // FlowLayout -> BorderLayout: las pestañas ocupan todo
        JTabbedPane tabs = element.createTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);   // pestañas en una sola fila
        JLabel statusBar = element.createStatusBar("Listo");
        tablePanel.add(tabs, BorderLayout.CENTER);
        tablePanel.add(statusBar, BorderLayout.PAGE_END);

        // Pestaña "Texto": formulario etiqueta | campo, pegado arriba para que no se estire
        JPanel form = element.createTitledPanel("Datos", new GridLayout(0, 2, 5, 5));
        JLabel nameLabel = element.createFieldLabel("Nombre:");
        JTextField nameField = element.createTextField(15);
        JLabel passwordLabel = element.createFieldLabel("Contraseña:");
        JPasswordField passwordField = element.createPasswordField(15);
        JLabel notesLabel = element.createFieldLabel("Notas:");
        JTextArea notesArea = element.createTextArea(3, 15);
        JScrollPane notesScroll = element.scroll(notesArea);
        form.add(nameLabel);
        form.add(nameField);
        form.add(passwordLabel);
        form.add(passwordField);
        form.add(notesLabel);
        form.add(notesScroll);
        JPanel textTab = new JPanel(new BorderLayout());
        textTab.add(form, BorderLayout.PAGE_START);
        tabs.addTab("Texto", textTab);

        // Pestaña "Label": createLabel (tamaño 0 x 0) solo se ve donde el gestor lo estira
        JLabel bigLabel = element.createLabel("Label que ocupa todo");
        tabs.addTab("Label", bigLabel);

        // Pestaña "Lista"
        JList<String> list = element.createList("Uno", "Dos", "Tres");
        JScrollPane listScroll = element.scroll(list);
        tabs.addTab("Lista", listScroll);

        // Pestañas "Tabla": con datos de ejemplo, y con un modelo propio que se puede modificar
        JScrollPane sampleTable = element.createTable();
        tabs.addTab("Tabla", sampleTable);

        DefaultTableModel model = new DefaultTableModel(new Object[] {"Nombre", "Valor"}, 0);
        model.addRow(new Object[] {"A", 10});
        model.addRow(new Object[] {"B", 20});
        JScrollPane modelTable = element.createTable(model);
        tabs.addTab("Modelo", modelTable);

        // Pestaña "Dividido": dos componentes con barra arrastrable (cada uno, un objeto nuevo)
        JList<String> leftList = element.createList("Izquierda 1", "Izquierda 2");
        JScrollPane leftScroll = element.scroll(leftList);
        JTextArea rightArea = element.createTextArea(3, 10);
        JScrollPane rightScroll = element.scroll(rightArea);
        JSplitPane splitPane = element.createSplitPane(leftScroll, rightScroll, 0.3);
        tabs.addTab("Dividido", splitPane);

        // Pestaña "Dibujo": panel Swing que dibuja (alternativa a Canvas)
        JPanel drawingPanel = element.createDrawingPanel(Color.WHITE, (g, size) -> {
            g.setColor(Color.ORANGE);
            g.fillOval(10, 10, size.width - 20, size.height - 20);
        });
        tabs.addTab("Dibujo", drawingPanel);

        // 4.3 Recuadro inferior (FlowLayout): elementos en fila, pasan a la siguiente si no caben
        JButton newButton = element.createButton("Nuevo");
        JButton saveButton = element.createButton("Guardar", e -> System.out.println("Guardando..."));
        JToolBar toolBar = element.createToolBar(newButton, saveButton);

        JCheckBox activeCheck = element.createCheckBox("Activo", true);

        ButtonGroup speed = new ButtonGroup();
        JPanel speedRadios = element.createRadioGroup(speed, "Lento", "Rápido");

        JComboBox<String> colorCombo = element.createComboBox("Rojo", "Verde", "Azul");
        JSpinner spinner = element.createSpinner(5, 0, 10, 1);
        JSlider slider = element.createSlider(0, 100, 50);

        JProgressBar progress = element.createProgressBar(0, 100);
        progress.setValue(40);

        JButton choiceButton = element.createButton("Opción elegida",
                e -> System.out.println(speed.getSelection().getActionCommand()));

        buttonsPanel.add(toolBar);
        buttonsPanel.add(activeCheck);
        buttonsPanel.add(speedRadios);
        buttonsPanel.add(colorCombo);
        buttonsPanel.add(spinner);
        buttonsPanel.add(slider);
        buttonsPanel.add(progress);
        buttonsPanel.add(choiceButton);

        // 4.4 Recuadro derecho: el Canvas creado en el paso 3 (createCanvas)
        // ---------------------------------------------------------------

        frame.setSize(1200,720);
        frame.setMinimumSize(new Dimension(800,500));
        frame.setLocationRelativeTo(null);  // centra la ventana en la pantalla
        frame.setVisible(true);
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