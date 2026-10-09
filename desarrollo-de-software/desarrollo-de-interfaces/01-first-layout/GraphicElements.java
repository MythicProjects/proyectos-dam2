import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

/**
 * Fábrica de componentes para probar ventanas.
 * Cada método crea un componente ya configurado y lo devuelve listo para añadir.
 * Los métodos originales se conservan con el mismo comportamiento.
 */
public class GraphicElements {

    private static final Border LINE = BorderFactory.createLineBorder(Color.GRAY);

    // =====================================================================
    // Utilidades
    // =====================================================================

    /**
     * Pone el tamaño preferido a 0 x 0 para que, dentro de un GridBagLayout,
     * el tamaño lo decidan solo los weights (reparto exacto por porcentajes).
     * Hay que aplicarlo a TODOS los componentes de las columnas o filas implicadas.
     */
    public <T extends JComponent> T flexible(T component) {
        component.setPreferredSize(new Dimension(0, 0));
        return component;
    }

    /** Envuelve un componente en un panel de desplazamiento (JTextArea, JList, JTable...). */
    public JScrollPane scroll(Component component) {
        return new JScrollPane(component);
    }

    // =====================================================================
    // Contenedores
    // =====================================================================

    public JPanel createPanel() {
        JPanel panel = new JPanel();                 // FlowLayout por defecto
        panel.setBorder(LINE);
        return panel;
    }

    /** Panel con el gestor de diseño indicado: new BorderLayout(), new GridBagLayout()... */
    public JPanel createPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBorder(LINE);
        return panel;
    }

    /** Panel con borde con título, para agrupar componentes relacionados. */
    public JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    /** Divide el espacio en dos con una barra arrastrable. resizeWeight: 0 crece la derecha, 1 la izquierda. */
    public JSplitPane createSplitPane(Component left, Component right, double resizeWeight) {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(resizeWeight);
        split.setContinuousLayout(true);
        return split;
    }

    /** Pestañas vacías; se añaden con tabs.addTab("Título", componente). */
    public JTabbedPane createTabbedPane() {
        return new JTabbedPane();
    }

    // =====================================================================
    // Texto
    // =====================================================================

    public JLabel createLabel(String title) {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setBorder(LINE);
        label.setPreferredSize(new Dimension(0, 0)); // el tamaño lo deciden los weights
        return label;
    }

    /** Etiqueta normal, a su tamaño natural (para formularios). */
    public JLabel createFieldLabel(String text) {
        return new JLabel(text);
    }

    /** Campo de una línea; columns fija el ancho preferido, no limita los caracteres. */
    public JTextField createTextField(int columns) {
        return new JTextField(columns);
    }

    /** Campo de contraseña; leer con getPassword() y borrar el array después. */
    public JPasswordField createPasswordField(int columns) {
        return new JPasswordField(columns);
    }

    /** Área de texto con salto de línea por palabras. Añadir con scroll(area). */
    public JTextArea createTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    /** Barra de estado: línea superior y margen interior. Va en BorderLayout.PAGE_END. */
    public JLabel createStatusBar(String text) {
        JLabel status = new JLabel(text);
        status.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        return status;
    }

    // =====================================================================
    // Botones y selección
    // =====================================================================

    public JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(e -> System.out.println("Pulsado: " + text));
        return button;
    }

    /** Botón con la acción que se le pase: createButton("Guardar", e -> guardar()). */
    public JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    public JCheckBox createCheckBox(String text, boolean selected) {
        return new JCheckBox(text, selected);
    }

    /**
     * Grupo de botones de opción excluyentes, dentro de un panel en fila.
     * La opción elegida se lee con group.getSelection().getActionCommand().
     */
    public JPanel createRadioGroup(ButtonGroup group, String... options) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 0));
        for (int i = 0; i < options.length; i++) {
            JRadioButton radio = new JRadioButton(options[i], i == 0);   // la primera, seleccionada
            radio.setActionCommand(options[i]);
            group.add(radio);
            panel.add(radio);
        }
        return panel;
    }

    /** Lista desplegable; getSelectedItem() devuelve Object. */
    public JComboBox<String> createComboBox(String... items) {
        return new JComboBox<>(items);
    }

    // =====================================================================
    // Valores numéricos
    // =====================================================================

    /** Deslizador con marcas y números cada cuarto del rango. */
    public JSlider createSlider(int min, int max, int value) {
        JSlider slider = new JSlider(min, max, value);
        slider.setMajorTickSpacing(Math.max(1, (max - min) / 4));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    /** Selector numérico. Ojo: setValue() desde el código no comprueba los límites. */
    public JSpinner createSpinner(int value, int min, int max, int step) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, step));
    }

    /** Barra de progreso con el porcentaje visible. */
    public JProgressBar createProgressBar(int min, int max) {
        JProgressBar bar = new JProgressBar(min, max);
        bar.setStringPainted(true);
        return bar;
    }

    // =====================================================================
    // Listas y tablas
    // =====================================================================

    /**
     * Lista modificable: los elementos se añaden o quitan con
     * ((DefaultListModel<String>) list.getModel()).addElement("...").
     * Añadir con scroll(list).
     */
    public JList<String> createList(String... items) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String item : items) {
            model.addElement(item);
        }
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

    public JScrollPane createTable() {
        String[] columns = {"Hilo", "Estado", "Iteraciones"};
        Object[][] data = {
                {"Hilo-1", "Ejecutando", 120},
                {"Hilo-2", "Pausado", 85},
                {"Hilo-3", "Terminado", 200}
        };
        return flexible(createTable(new DefaultTableModel(data, columns))); // el tamaño lo deciden los weights
    }

    /**
     * Tabla a partir de un modelo. Quien llama conserva el modelo y puede
     * cambiar los datos con model.addRow(...), model.setValueAt(...), model.setRowCount(0)...
     * Se devuelve dentro de un JScrollPane para que se vean los encabezados.
     */
    public JScrollPane createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);   // el fondo blanco ocupa todo el panel
        return new JScrollPane(table);
    }

    // =====================================================================
    // Dibujo
    // =====================================================================

    public Canvas createCanvas(){
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.blue);
        canvas.setPreferredSize(new Dimension(0,0));
        return canvas;
    }

    /**
     * Alternativa Swing a Canvas: un JPanel que dibuja con el código que se le pase.
     * Ejemplo:
     *   createDrawingPanel(Color.WHITE, (g, size) -> g.fillOval(0, 0, size.width, size.height));
     * Para redibujar tras cambiar los datos, llamar a panel.repaint().
     */
    public JPanel createDrawingPanel(Color background, BiConsumer<Graphics2D, Dimension> painter) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);                     // borra el fondo
                Graphics2D g2 = (Graphics2D) g.create();     // copia: los cambios no se propagan
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                painter.accept(g2, getSize());
                g2.dispose();
            }
        };
        panel.setBackground(background);
        panel.setPreferredSize(new Dimension(0, 0));         // el tamaño lo deciden los weights
        return panel;
    }

    // =====================================================================
    // Menús y barra de herramientas
    // =====================================================================

    /** Opción de menú con acelerador portable (Ctrl, o Cmd en macOS). key: KeyEvent.VK_O... */
    public JMenuItem createMenuItem(String text, int key, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        int shortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        item.setAccelerator(KeyStroke.getKeyStroke(key, shortcut));
        item.addActionListener(action);
        return item;
    }

    /** Menú con mnemónico (Alt + letra). Un null en items añade un separador. */
    public JMenu createMenu(String title, int mnemonic, JMenuItem... items) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        for (JMenuItem item : items) {
            if (item == null) {
                menu.addSeparator();
            } else {
                menu.add(item);
            }
        }
        return menu;
    }

    /** Barra de menús lista para frame.setJMenuBar(...). */
    public JMenuBar createMenuBar(JMenu... menus) {
        JMenuBar bar = new JMenuBar();
        for (JMenu menu : menus) {
            bar.add(menu);
        }
        return bar;
    }

    /** Barra de herramientas fija. Va en BorderLayout.PAGE_START. */
    public JToolBar createToolBar(Component... items) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (Component item : items) {
            toolBar.add(item);
        }
        return toolBar;
    }
}

// =====================================================================
//      OLD
// =====================================================================

/*import javax.swing.*;
import java.awt.*;

public  class GraphicElements{
    // Create components
    public JLabel createLabel(String title) {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label.setPreferredSize(new Dimension(0, 0)); // el tamaño lo deciden los weights
        return label;
    }

    public Canvas createCanvas(){
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.blue);
        canvas.setPreferredSize(new Dimension(0,0));
        return canvas;
    }

    public JPanel createPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return panel;
    }

    public JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(e -> System.out.println("Pulsado: " + text));
        return button;
    }

    public JScrollPane createTable() {
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
}*/