package view.components;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import view.dto.ProducerSlotVM;
import view.theme.Theme;
import view.theme.Themed;

public class ProducersPanel extends JPanel implements Themed{
    private static final int ROWS = 2;
    private static final int COLUMNS = 4;

    private final ProducerCell[] cells = new ProducerCell[ROWS * COLUMNS];

    public ProducersPanel(){
        super(new GridLayout(ROWS, COLUMNS, 6, 6));

        for (int i = 0; i < cells.length; i++) {
            cells[i] = new ProducerCell();
            add(cells[i]);
        }
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setSlots(List<ProducerSlotVM> slots) {
        for (int i = 0; i < cells.length && i < slots.size(); i++) {
            cells[i].setSlot(slots.get(i));
        }
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        setBackground(theme.paper());
        for (ProducerCell cell : cells) {
            cell.applyTheme(theme);
        }
    }
}
