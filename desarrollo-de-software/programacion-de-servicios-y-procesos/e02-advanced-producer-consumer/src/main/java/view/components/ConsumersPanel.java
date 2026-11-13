package view.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.dto.ConsumerSlotVM;
import view.theme.Theme;
import view.theme.Themed;

public class ConsumersPanel extends JPanel implements Themed {
    private static final int ROWS = 8;
    private static final int COLUMNS = 4;

    private final JPanel grid = new JPanel(new GridLayout(ROWS, COLUMNS, 4, 4));
    private final ConsumerCell[] cells = new ConsumerCell[ROWS * COLUMNS];

    private final JPanel footer = new JPanel(new BorderLayout());
    private final JLabel seatedLabel = new JLabel();
    private final JLabel queueLabel = new JLabel();

    public ConsumersPanel(){
        super(new BorderLayout(0, 6));

        for (int i = 0; i < cells.length; i++) {
            cells[i] = new ConsumerCell();
            grid.add(cells[i]);
        }

        footer.add(seatedLabel, BorderLayout.WEST);
        footer.add(queueLabel, BorderLayout.EAST);

        add(grid, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        setConsumers(List.of(), 0);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setConsumers(List<ConsumerSlotVM> slots, int queue) {
        int seated = 0;
        for (int i = 0; i < cells.length && i < slots.size(); i++) {
            cells[i].setSlot(slots.get(i));
            if (slots.get(i).busy()) seated++;
        }
        seatedLabel.setText("Seated " + seated + "/" + cells.length);
        queueLabel.setText("Queue " + queue);
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme) {
        setBackground(theme.paper());
        grid.setBackground(theme.paper());
        footer.setBackground(theme.paper());
        for (ConsumerCell cell : cells) {
            cell.applyTheme(theme);
        }
        for (JLabel label : new JLabel[] {seatedLabel, queueLabel}) {
            label.setForeground(theme.ink());
            label.setFont(theme.uiFont());
        }
    }
}
