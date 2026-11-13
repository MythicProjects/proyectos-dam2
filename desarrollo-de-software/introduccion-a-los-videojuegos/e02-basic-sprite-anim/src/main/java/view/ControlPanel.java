package view;

import java.awt.Component;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/** A vertical list of labelled sliders. */
final class ControlPanel extends JPanel {

    ControlPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    void addTitle(String text) {
        JLabel title = new JLabel(text);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 2, 0));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);
    }

    /** Adds "label: value" and a slider; every change is passed to onChange. */
    void addSlider(String label, int min, int max, int value, IntConsumer onChange) {
        JLabel text = new JLabel(label + ": " + value);
        JSlider slider = new JSlider(min, max, value);
        slider.addChangeListener(e -> {
            text.setText(label + ": " + slider.getValue());
            onChange.accept(slider.getValue());
        });
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(text);
        add(slider);
    }
}
