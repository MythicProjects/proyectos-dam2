package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import presenter.IPresenter;
import view.dto.ResourceRow;
import view.dto.Settings;

public class View extends JFrame implements IView {
    private static final String SLEEP = "sleep";
    private static final String WAIT_NOTIFY = "wait / notify";

    // Settings
    private final JSpinner producers = spinner(2, 1, 50);
    private final JSpinner consumers = spinner(2, 1, 50);
    private final JSpinner resourceTypes = spinner(1, 1, 10);
    private final JSpinner minProduceMs = spinner(200, 0, 10000);
    private final JSpinner maxProduceMs = spinner(800, 0, 10000);
    private final JSpinner minConsumeMs = spinner(200, 0, 10000);
    private final JSpinner maxConsumeMs = spinner(800, 0, 10000);
    private final JSpinner produceAmount = spinner(1, 1, 100);
    private final JSpinner consumeAmount = spinner(1, 1, 100);
    private final JSpinner minStock = spinner(0, -1000, 1000);
    private final JSpinner maxStock = spinner(10, -1000, 1000);
    private final JCheckBox stockControl = new JCheckBox("Stock control", true);
    private final JCheckBox synchronize = new JCheckBox("Synchronized", true);
    private final JComboBox<String> waitMode = new JComboBox<>(new String[] {WAIT_NOTIFY, SLEEP});

    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");

    // Results
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] {"Resource", "Stock", "Expected", "Produced", "Consumed", "OK"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public View() {
        super("Producer-Consumer Simulator");

        add(buildSettingsPanel(), BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // wait/notify needs synchronized: the option is only available with it
        synchronize.addActionListener(e -> waitMode.setEnabled(synchronize.isSelected()));

        setRunning(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);
    }

    private JPanel buildSettingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int row = 0;
        row = addRow(panel, row, "Producers", producers);
        row = addRow(panel, row, "Consumers", consumers);
        row = addRow(panel, row, "Resource types", resourceTypes);
        row = addRow(panel, row, "Min produce time (ms)", minProduceMs);
        row = addRow(panel, row, "Max produce time (ms)", maxProduceMs);
        row = addRow(panel, row, "Min consume time (ms)", minConsumeMs);
        row = addRow(panel, row, "Max consume time (ms)", maxConsumeMs);
        row = addRow(panel, row, "Units produced", produceAmount);
        row = addRow(panel, row, "Units consumed", consumeAmount);
        row = addRow(panel, row, "Min stock", minStock);
        row = addRow(panel, row, "Max stock", maxStock);
        row = addRow(panel, row, "", stockControl);
        row = addRow(panel, row, "", synchronize);
        row = addRow(panel, row, "Wait mode", waitMode);

        JPanel buttons = new JPanel();
        buttons.add(startButton);
        buttons.add(stopButton);
        addRow(panel, row, "", buttons);

        return panel;
    }

    private static int addRow(JPanel panel, int row, String text, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        panel.add(new JLabel(text), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, c);
        return row + 1;
    }

    private static JSpinner spinner(int value, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, 1));
    }

    private static int value(JSpinner spinner) {
        return (Integer) spinner.getValue();
    }

    // ------------------------------------------------------------------
    // IView
    // ------------------------------------------------------------------
    @Override
    public void setPresenter(IPresenter presenter) {
        startButton.addActionListener(e -> presenter.onStart());
        stopButton.addActionListener(e -> presenter.onStop());
    }

    @Override
    public void showView() {
        setVisible(true);
    }

    @Override
    public Settings getSettings() {
        return new Settings(
                value(producers), value(consumers), value(resourceTypes),
                value(minProduceMs), value(maxProduceMs),
                value(minConsumeMs), value(maxConsumeMs),
                value(produceAmount), value(consumeAmount),
                value(minStock), value(maxStock),
                stockControl.isSelected(),
                synchronize.isSelected(),
                WAIT_NOTIFY.equals(waitMode.getSelectedItem()));
    }

    @Override
    public void showResources(List<ResourceRow> rows) {
        tableModel.setRowCount(0);
        for (ResourceRow r : rows) {
            boolean ok = r.stock() == r.expected();
            tableModel.addRow(new Object[] {
                    r.id(), r.stock(), r.expected(), r.produced(), r.consumed(), ok ? "yes" : "NO"});
        }
    }

    @Override
    public void setRunning(boolean running) {
        startButton.setEnabled(!running);
        stopButton.setEnabled(running);
        for (JComponent field : new JComponent[] {producers, consumers, resourceTypes,
                minProduceMs, maxProduceMs, minConsumeMs, maxConsumeMs,
                produceAmount, consumeAmount, minStock, maxStock, stockControl, synchronize}) {
            field.setEnabled(!running);
        }
        waitMode.setEnabled(!running && synchronize.isSelected());
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
