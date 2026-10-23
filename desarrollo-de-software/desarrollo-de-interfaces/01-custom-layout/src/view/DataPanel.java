package view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import presenter.IPresenter;

public class DataPanel extends JPanel {
    private IPresenter presenter;
    private boolean updating = false;
    // ------------------------------------------------------------------
    // Interface elements
    // ------------------------------------------------------------------
    private final JComboBox<ResolutionPreset> resolutionBox;
    private final JSpinner widthJSpinner;
    private final JSpinner heightJSpinner;
    // ------------------------------------------------------------------
    // Resolution
    // ------------------------------------------------------------------
    // Una resolución con nombre. toString() es lo que enseña el desplegable
    private record ResolutionPreset(String name, int width, int height) {
        @Override
        public String toString() {
            return width == 0 ? name : name + "  " + width + "×" + height;
        }
    }
    private static final ResolutionPreset CUSTOM = new ResolutionPreset("Personalizada", 0, 0);
    private static final ResolutionPreset[] RESOLUTION_PRESETS = {
        CUSTOM,
        new ResolutionPreset("Atari 2600",        160, 192),
        new ResolutionPreset("Game Boy",          160, 144),
        new ResolutionPreset("NES",               256, 240),
        new ResolutionPreset("Master System",     256, 192),
        new ResolutionPreset("SNES",              256, 224),
        new ResolutionPreset("Mega Drive",        320, 224),
        new ResolutionPreset("Game Boy Advance",  240, 160),
        new ResolutionPreset("PlayStation",       320, 240),
        new ResolutionPreset("Nintendo DS",       256, 192),
        new ResolutionPreset("PSP",               480, 272),
        new ResolutionPreset("VGA",               640, 480),
        new ResolutionPreset("HD 720p",          1280, 720),
        new ResolutionPreset("Full HD 1080p",    1920, 1080),
    };
    // ------------------------------------------------------------------
    // Data Panel constructor
    // ------------------------------------------------------------------
    public DataPanel(){
        // ---------------------- Create Data Panel Layout
        setLayout(new GridLayout(0,2,8,8)); // filas automáticas, 2 columnas: etiqueta | control
        // ---------------------- Create components
        // --- Resolution
        resolutionBox = new JComboBox<>(RESOLUTION_PRESETS);
        widthJSpinner  = new JSpinner(new SpinnerNumberModel(720, 16, 4096, 10));
        heightJSpinner = new JSpinner(new SpinnerNumberModel(180, 16, 2160, 10));
        JButton applyButton = new JButton("Apply resolution");
        // ---------------------- Add components 
        // --- Resolution
        add(new JLabel("Screen"));
        add(resolutionBox);
        add(new JLabel("Width"));
        add(widthJSpinner);
        add(new JLabel("Height"));
        add(heightJSpinner);
        add(new JLabel(""));
        add(applyButton);
        // ---------------------- Action buttons
        resolutionBox.addActionListener(e -> applyPreset());
        applyButton.addActionListener(e -> {
            sendResolution();
            selectPresetFor((int) widthJSpinner.getValue(), (int) heightJSpinner.getValue());
        });
    }
    // ------------------------------------------------------------------
    // Presenter
    // ------------------------------------------------------------------
    public void setPresenter(IPresenter presenter){ this.presenter = presenter; }
    // ------------------------------------------------------------------
    // Initial values
    // ------------------------------------------------------------------
    public void showValues(int width, int height) {
        widthJSpinner.setValue(width);
        heightJSpinner.setValue(height);
        selectPresetFor(width, height);
    }
    // ------------------------------------------------------------------
    // Resolution settings
    // ------------------------------------------------------------------
    // ---------------------- Resolution Box Change
    private void applyPreset() {
        if (updating) return;

        ResolutionPreset preset = (ResolutionPreset) resolutionBox.getSelectedItem();
        if (preset == null || preset == CUSTOM) return;

        widthJSpinner.setValue(preset.width());
        heightJSpinner.setValue(preset.height());
        sendResolution();
    }
    // ---------------------- Apply Button change
    private void selectPresetFor(int width, int height) {
        ResolutionPreset match = CUSTOM;
        for (ResolutionPreset preset : RESOLUTION_PRESETS) {
            if (preset.width() == width && preset.height() == height) {
                match = preset;
                break;
            }
        }
        updating = true;
        resolutionBox.setSelectedItem(match);
        updating = false;
    }
    // ---------------------- Send resolution to model
    private void sendResolution() { presenter.setResolution((int) widthJSpinner.getValue(), (int) heightJSpinner.getValue()); }
}
