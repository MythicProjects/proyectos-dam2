package view.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.dto.ProducerSlotVM;
import view.theme.Icons;
import view.theme.Theme;
import view.theme.Themed;

public class ProducerCell extends JPanel implements Themed {
    public static final String OPEN = "open";
    public static final String EMPTY = "empty";

    private final CardLayout cards = new CardLayout();
    private final RetroBorder border = new RetroBorder(2, 2);

    private final JPanel openCard = new JPanel(new BorderLayout(4, 4));
    private final JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
    private final JLabel idLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final JLabel timeLabel = new JLabel();
    private final Swatch swatch = new Swatch();
    private final SegmentedBar bar = new SegmentedBar(6, 2, false);

    private final JLabel emptyLabel = new JLabel("Empty", Icons.LOCK, SwingConstants.CENTER);

    private Theme theme = Theme.SNES;
    private ProducerSlotVM slot;

    public ProducerCell(){
        setLayout(cards);
        setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        header.setOpaque(false);
        header.add(idLabel);
        header.add(swatch);
        header.add(nameLabel);

        openCard.setOpaque(false);
        openCard.add(header, BorderLayout.NORTH);
        openCard.add(timeLabel, BorderLayout.CENTER);
        openCard.add(bar, BorderLayout.SOUTH);

        emptyLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        emptyLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        add(openCard, OPEN);
        add(emptyLabel, EMPTY);
        cards.show(this, EMPTY);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setSlot(ProducerSlotVM slot){
        this.slot = slot;
        if (!slot.open()) {
            cards.show(this, EMPTY);
            return;
        }

        idLabel.setText("#" + (slot.index() + 1));
        nameLabel.setText("Lv " + slot.level());
        timeLabel.setText(String.format("%.1f s", slot.cycleSeconds()));
        swatch.setPaint(theme.productPaint(slot.productID()));
        bar.setFill(theme.productPaint(slot.productID()));
        bar.setProgress(slot.progress());
        cards.show(this, OPEN);
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        this.theme = theme;
        border.applyTheme(theme);
        setBackground(theme.paper());
        for (JLabel label : new JLabel[] { idLabel, nameLabel, timeLabel, emptyLabel}) {
            label.setForeground(theme.ink());
            label.setFont(theme.uiFont());
        }

        timeLabel.setFont(theme.numberFont());
        emptyLabel.setForeground(theme.muted());
        swatch.setForeground(theme.ink());
        bar.setForeground(theme.muted());
        if(slot != null) setSlot(slot);
        repaint();
    }
}
