package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import common.PaletteType;
import presenter.IPresenter;

public class ControlPanel extends JPanel{
    // Colores de la rejilla: 2 filas de 8

    private static final int[] SWATCH_COLORS = {
        0xFFFF0000, 0xFFFF4000, 0xFFFF8000, 0xFFFFC000, 0xFFFFFF00, 0xFF80FF00, 0xFF00FF00, 0xFF00FF80,
        0xFF00FFFF, 0xFF0080FF, 0xFF0040FF, 0xFF4000FF, 0xFF8000FF, 0xFFBF00FF, 0xFFFF00FF, 0xFFFF0080
    };

    private static final int COLUMNS = 8;
    private static final int SWATCH_SIZE = 22;
    private static final int PRESET_HEIGHT = 26;

    private static final Border NORMAL_BORDER = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
    private static final Border SELECTED_BORDER = BorderFactory.createLineBorder(Color.WHITE, 2);

    private IPresenter presenter;
    private JLabel selected; // casilla marcada ahora mismo: puede ser un preset o un color de la rejilla

    public ControlPanel(){
        setLayout(new BorderLayout(0,8)); // Configura como se colocara
        add(createActionButtons(), BorderLayout.NORTH);
        add(createPaletteSelector(), BorderLayout.CENTER);
    }

    public void setPresenter(IPresenter presenter){
        this.presenter = presenter;
    }

    // ------------------------------------------------------------------
    // Play / Pause / Stop
    // ------------------------------------------------------------------
    private JPanel createActionButtons(){
        JPanel panel = new JPanel(new FlowLayout());

        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(e -> presenter.play());
        pauseButton.addActionListener(e -> presenter.togglePause());
        stopButton.addActionListener(e -> presenter.stop());

        panel.add(playButton);
        panel.add(pauseButton);
        panel.add(stopButton);

        return panel;
    }

    // ------------------------------------------------------------------
    // Paletas predefinidas (arriba) + rejilla de colores (abajo)
    // ------------------------------------------------------------------

    private JPanel createPaletteSelector() {
        JPanel selector = new JPanel(new BorderLayout(0, 6));
        selector.add(createPresetRow(), BorderLayout.NORTH);
        selector.add(createColorGrid(), BorderLayout.CENTER);
        return selector;
    }

    private JPanel createPresetRow() {
        PaletteType[] types = PaletteType.values();
        JPanel row = new JPanel(new GridLayout(1, types.length, 3, 3)); // una fila, una columna por paleta

        for (PaletteType type : types) {
            JLabel preset = createTile(type.getPreviewColor());
            preset.setText(type.getLabel());
            preset.setHorizontalAlignment(SwingConstants.CENTER);
            preset.setForeground(Color.WHITE);
            preset.setPreferredSize(new Dimension(0, PRESET_HEIGHT));

            preset.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    markSelected(preset);
                    presenter.setPaletteType(type);
                }
            });
            row.add(preset);

            if (type == PaletteType.FIRE) {
                markSelected(preset); // la paleta con la que arranca el modelo
            }
        }
        return row;
    }

    private JPanel createColorGrid() {
        JPanel grid = new JPanel(new GridLayout(0, COLUMNS, 3, 3)); // filas automáticas

        for (int color : SWATCH_COLORS) {
            JLabel swatch = createTile(color);
            swatch.setPreferredSize(new Dimension(SWATCH_SIZE, SWATCH_SIZE));

            swatch.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    markSelected(swatch);
                    presenter.setPaletteColor(color);
                }
            });
            grid.add(swatch);
        }
        return grid;
    }

    // Casilla de color: base común para presets y rejilla
    private JLabel createTile(int color) {
        JLabel tile = new JLabel();
        tile.setOpaque(true);                 // sin esto, el fondo no se pinta
        tile.setBackground(new Color(color));
        tile.setBorder(NORMAL_BORDER);
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return tile;
    }

    private void markSelected(JLabel tile) {
        if (selected != null) {
            selected.setBorder(NORMAL_BORDER); // desmarca la anterior
        }
        selected = tile;
        tile.setBorder(SELECTED_BORDER);       // marca la nueva
    }
}