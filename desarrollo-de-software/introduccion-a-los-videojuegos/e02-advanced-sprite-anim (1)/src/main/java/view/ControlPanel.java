package view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/** Side panel of labelled sliders. Each slider reports its value to a callback. */
final class ControlPanel extends JPanel {

    ControlPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(200, 0));
    }

    /** Adds "Title: N fps" plus a slider; every change goes to onChange. */
    void addSlider(String title, int min, int max, int value, String unit, IntConsumer onChange) {
        JLabel label = new JLabel(text(title, value, unit));
        JSlider slider = new JSlider(min, max, value);
        slider.setFocusable(false);

        slider.addChangeListener(e -> {
            int current = slider.getValue();
            label.setText(text(title, current, unit));
            onChange.accept(current);
        });

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        add(slider);
        add(Box.createVerticalStrut(16));
    }

    private static String text(String title, int value, String unit) {
        return title + ": " + value + " " + unit;
    }
}